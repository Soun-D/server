package com.sound.sound;

import com.sound.sound.dto.EmailRequest;
import com.sound.sound.dto.SiteSoundRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/sound")
@RequiredArgsConstructor
public class SoundController {

    private final SoundService soundService;

    @PostMapping("/file")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadAudioFile(@RequestPart(value = "mp3") MultipartFile audioFile,
                                @Valid @RequestPart(value = "email") EmailRequest emailRequest) {
        soundService.uploadAudioFile(audioFile, emailRequest);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveSiteSound(@Valid @RequestBody SiteSoundRequest soundRequest) {
        soundService.saveSiteSound(soundRequest);
    }
}
