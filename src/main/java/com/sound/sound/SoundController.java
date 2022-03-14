package com.sound.sound;

import com.sound.sound.exception.SoundException;
import com.sound.sound.mp3.FileUploadProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController("/sound")
@RequiredArgsConstructor
public class SoundController {

    private final FileUploadProvider fileUploadProvider;

    @PostMapping("/file")
    public void uploadMp3(@RequestParam(value = "mp3")MultipartFile mp3) {
        String fileExtension = fileUploadProvider.getFileExtension(Objects.requireNonNull(mp3.getOriginalFilename()));
        if (!fileExtension.equals("mp3")) throw new SoundException(400, "유효하지 않은 파일 확장자입니다.");
        fileUploadProvider.uploadFile(mp3);
    }
}
