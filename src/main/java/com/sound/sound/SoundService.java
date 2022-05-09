package com.sound.sound;

import com.sound.sound.dto.request.EmailRequest;
import com.sound.sound.dto.request.SiteSoundRequest;
import com.sound.sound.dto.response.AudioFileResponse;
import com.sound.sound.dto.response.SiteSoundResponse;
import com.sound.sound.entity.AudioFile;
import com.sound.sound.entity.SiteSound;
import com.sound.sound.entity.User;
import com.sound.sound.exception.SoundException;
import com.sound.sound.mp3.FileUploadProvider;
import com.sound.sound.repository.AudioFileRepository;
import com.sound.sound.repository.SiteSoundRepository;
import com.sound.sound.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.*;

@Service
@RequiredArgsConstructor
public class SoundService {

    private final SiteSoundRepository siteSoundRepository;
    private final FileUploadProvider fileUploadProvider;
    private final UserRepository userRepository;
    private final AudioFileRepository audioFileRepository;

    @Transactional
    public void uploadAudioFile(MultipartFile audioFile, EmailRequest request) {
        User user;
        String email = request.getEmail();

        if (userRepository.existsByEmail(email))
            user = userRepository.findByEmail(email);
        else
            user = saveUser(email);

        String fileExtension = audioFile.getName();

        isFileExtensionMp3(fileExtension);

        String fileLocation = fileUploadProvider.uploadFileToS3(audioFile);

        audioFileRepository.save(AudioFile.builder()
                .fileLocation(fileLocation)
                .fileName(
                        requireNonNull(
                                audioFile.getOriginalFilename()
                        ).split("\\.")[0]
                )
                .user(user)
                .build());
    }

    @Transactional
    public void saveSiteSound(SiteSoundRequest request) {
        String[] urls = request.getUrl()
                .replaceAll("\\s", "")
                .split(",");

        for (String url : urls) {
            checkUrlDuplicate(url, request.getAudioFileId());

            siteSoundRepository.save(SiteSound.builder()
                            .url(url)
                            .audioFile(getAudioFileById(request.getAudioFileId()))
                            .build());
        }
    }

    public List<AudioFileResponse> queryAudioFile(String email) {
        return audioFileRepository.findAllByUserEmail(email)
                .stream().map(AudioFileResponse::of)
                .collect(Collectors.toList());
    }

    public List<SiteSoundResponse> querySiteSound(String email) {
        return siteSoundRepository.findAllByAudioFileUserEmail(email).stream()
                .map(SiteSoundResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAudioFile(Integer audioFileId, String email) {
        fileUploadProvider.deleteFile(
                getAudioFileById(audioFileId).getFileLocation());
        audioFileRepository.deleteByIdAndUserEmail(audioFileId, email);
    }

    @Transactional
    public void deleteSiteSound(Integer siteSoundId) {
        siteSoundRepository.deleteById(siteSoundId);
    }


    private void isFileExtensionMp3(String fileExtension) {
        if (!fileExtension.equals("mp3"))
            throw new SoundException(400, "{ " + fileExtension + " } 는 유효하지 않은 파일 형식입니다.");
    }

    private User saveUser(String email) {
        return userRepository.save(new User(email));
    }

    private AudioFile getAudioFileById(Integer audioFileId) {
        return audioFileRepository.findById(audioFileId)
                .orElseThrow(() -> new SoundException(404, "id : "
                        + audioFileId + " audio 파일을 찾을 수 없습니다."));
    }

    private void checkUrlDuplicate(String url, Integer audioFileId) {
        if (siteSoundRepository.existsByUrlAndAudioFile_id(url, audioFileId))
            throw new SoundException(409, "이미 이 URL { " + audioFileId + " }에 매치된 오디오 파일이 존재합니다.");
    }
}
