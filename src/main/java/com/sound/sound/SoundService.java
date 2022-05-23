package com.sound.sound;

import com.sound.sound.dto.request.AudioFileRequest;
import com.sound.sound.dto.request.SiteSoundRequest;
import com.sound.sound.dto.request.SiteSoundUpdateRequest;
import com.sound.sound.dto.request.YoutubeRequest;
import com.sound.sound.dto.response.AudioFileResponse;
import com.sound.sound.dto.response.SiteSoundResponse;
import com.sound.sound.entity.*;
import com.sound.sound.exception.SoundException;
import com.sound.sound.mp3.FileUploadProvider;
import com.sound.sound.repository.AudioFileRepository;
import com.sound.sound.repository.SiteSoundRepository;
import com.sound.sound.repository.UrlRepository;
import com.sound.sound.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Objects.*;

@Service
@RequiredArgsConstructor
public class SoundService {

    private final SiteSoundRepository siteSoundRepository;
    private final FileUploadProvider fileUploadProvider;
    private final UserRepository userRepository;
    private final AudioFileRepository audioFileRepository;
    private final UrlRepository urlRepository;

    @Transactional
    public void uploadAudioFile(MultipartFile audioFile, AudioFileRequest request) {

        String email = request.getEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.orElseGet(() -> saveUser(email));

        String title = requireNonNull(
                audioFile.getOriginalFilename()
        ).split("\\.")[0];

        if (audioFileRepository.existsByTitleAndUser(title, user))
            throw new SoundException(409, "동일한 이름의 파일이 이미 존재합니다.");

        String fileExtension = audioFile.getName();

        if (!fileExtension.equals("mp3") && !fileExtension.equals("m4a"))
            throw new SoundException(400, "{ " + fileExtension + " } 는 유효하지 않은 파일 형식입니다.");

        String src = fileUploadProvider.uploadFileToS3(audioFile);

        audioFileRepository.save(AudioFile.builder()
                .src(src)
                .title(title)
                .user(user)
                .playTime(request.getPlayTime())
                .isYoutube(false)
                .build());
    }

    @Transactional
    public void saveYoutube(YoutubeRequest request) {

        String email = request.getEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.orElseGet(() -> saveUser(email));

        audioFileRepository.save(AudioFile.builder()
                .playTime(request.getPlayTime())
                .isYoutube(true)
                .title(request.getTitle())
                .src(request.getSrc())
                .user(user)
                .build());
    }

    @Transactional
    public void saveSiteSound(SiteSoundRequest request) {
        String[] requestedUrls = request.getUrl()
                .replaceAll("\\s", "")
                .split(",");

        AudioFile audioFile = getAudioFileById(request.getAudioFileId());

        User user = audioFile.getUser();

        saveEachUrl(requestedUrls, user);

        siteSoundRepository.save(SiteSound.builder()
                .url(request.getUrl())
                .audioFile(audioFile)
                .build());
    }

    @Transactional
    public void updateSiteSound(SiteSoundUpdateRequest request) {
        SiteSound siteSound = getSiteSoundById(request.getId());

        String[] existsUrls = siteSound.getUrl()
                .replaceAll("\\s", "")
                .split(",");

        for (String url : existsUrls)
            urlRepository.deleteByUrlAndUser(url, siteSound.getAudioFile().getUser());

        User user = siteSound.getAudioFile().getUser();

        String[] newUrls = request.getUrl()
                .replaceAll("\\s", "")
                .split(",");

        saveEachUrl(newUrls, user);

        AudioFile audioFile = getAudioFileById(request.getAudioFileId());

        siteSoundRepository.save(siteSound.update(request.getUrl(), audioFile));
    }

    public List<AudioFileResponse> queryAllAudioFile(String email) {
        return audioFileRepository.findAllByUserEmailOrderById(email).stream()
                .map(AudioFileResponse::of)
                .collect(Collectors.toList());
    }

    public List<AudioFileResponse> queryAudioFile(String email) {
        return audioFileRepository.findAllByUserEmailAndIsYoutubeOrderById(email, false).stream()
                .map(AudioFileResponse::of)
                .collect(Collectors.toList());
    }

    public List<AudioFileResponse> queryYoutube(String email) {
        return audioFileRepository.findAllByUserEmailAndIsYoutubeOrderById(email, true).stream()
                .map(AudioFileResponse::of)
                .collect(Collectors.toList());
    }

    public List<SiteSoundResponse> querySiteSound(String email) {
        return siteSoundRepository.findAllByAudioFileUserEmailOrderById(email).stream()
                .map(SiteSoundResponse::of)
                .collect(Collectors.toList());
    }

    public List<SiteSoundResponse> querySplitSiteSound(String email) {
        List<SiteSoundResponse> responseList = new ArrayList<>();
        List<SiteSound> siteSoundList = siteSoundRepository.findAllByAudioFileUserEmailOrderById(email);
        siteSoundList
                .forEach(
                        siteSound -> {
                            String[] existsUrls = siteSound.getUrl()
                                    .replaceAll("\\s", "")
                                    .split(",");

                            if (existsUrls.length > 0)
                                for (String url : existsUrls)
                                    responseList.add(SiteSoundResponse.oneUrl(url, siteSound));
                        });

        return responseList;
    }

    @Transactional
    public void deleteAudioFile(Integer audioFileId, String email) {
        if (siteSoundRepository.existsByAudioFileId(audioFileId))
            throw new SoundException(400, "연결된 URL이 존재하므로 삭제할 수 없습니다.");

        AudioFile audioFile = getAudioFileById(audioFileId);

        audioFileRepository.deleteByIdAndUserEmail(audioFileId, email);

        fileUploadProvider.deleteFile(audioFile.getSrc());
    }

    @Transactional
    public void deleteSiteSound(Integer siteSoundId) {
        SiteSound siteSound = getSiteSoundById(siteSoundId);

        String[] existsUrls = siteSound.getUrl()
                .replaceAll("\\s", "")
                .split(",");

        for (String url : existsUrls)
            urlRepository.deleteByUrlAndUser(url, siteSound.getAudioFile().getUser());

        siteSoundRepository.deleteById(siteSoundId);
    }


    private User saveUser(String email) {
        return userRepository.save(new User(email));
    }

    private AudioFile getAudioFileById(Integer audioFileId) {
        return audioFileRepository.findById(audioFileId)
                .orElseThrow(() -> new SoundException(404, "id : "
                        + audioFileId + " audio 파일을 찾을 수 없습니다."));
    }

    private SiteSound getSiteSoundById(Integer siteSoundId) {
        return siteSoundRepository.findById(siteSoundId)
                .orElseThrow(() -> new SoundException(404, "Site Sound Not Found."));
    }

    private void saveEachUrl(String[] urls, User user) {
        for (String url : urls) {

            if (!Pattern.matches("^((http(s?))://)([0-9a-zA-Z\\-]+\\.)+[a-zA-Z]{2,6}(:[0-9]+)?(/\\S*)?$", url))
                throw new SoundException(400, "URL이 형식에 맞지 않습니다.");

            if (urlRepository.existsByUrlAndUser(url, user))
                throw new SoundException(409, "이미 이 URL { " + url + " }에 매치되어있는 소리가 존재합니다.");

            urlRepository.save(Url.builder()
                    .url(url)
                    .user(user)
                    .build());
        }
    }

}
