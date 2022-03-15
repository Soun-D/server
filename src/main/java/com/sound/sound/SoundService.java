package com.sound.sound;

import com.sound.sound.dto.EmailRequest;
import com.sound.sound.dto.SiteSoundRequest;
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
import org.springframework.web.multipart.MultipartFile;

import static java.util.Objects.*;

@Service
@RequiredArgsConstructor
public class SoundService {

    private final SiteSoundRepository siteSoundRepository;
    private final FileUploadProvider fileUploadProvider;
    private final UserRepository userRepository;
    private final AudioFileRepository audioFileRepository;

    public void uploadAudioFile(MultipartFile audioFile, EmailRequest emailRequest) {
        User user;
        String email = emailRequest.getEmail();

        if (userRepository.existsByEmail(email))
            user = userRepository.findByEmail(email);
        else
            user = userRepository.save(User.builder().email(email).build());

        String fileExtension = audioFile.getName();
        if (!fileExtension.equals("mp3"))
            throw new SoundException(400, "{ " + fileExtension + " } 는 유효하지 않은 파일 확장자입니다.");

        String fileLocation = fileUploadProvider.uploadFile(audioFile);

        audioFileRepository.save(AudioFile.builder()
                .fileLocation(fileLocation)
                .fileName(requireNonNull(audioFile.getOriginalFilename()).split("\\.")[0])
                .user(user)
                .build());
    }

    public void saveSiteSound(SiteSoundRequest soundDto) {

        siteSoundRepository.save(
                SiteSound.builder()
                        .url(soundDto.getUrl())
                        .audioFile(
                                audioFileRepository.findById(soundDto.getAudioFileId())
                                        .orElseThrow(() -> new SoundException(404, "id : " + soundDto.getAudioFileId() + " audio 파일을 찾을 수 없습니다.")))
                        .build());
    }
}
