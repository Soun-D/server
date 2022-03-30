package com.sound.sound;

import com.sound.sound.dto.request.EmailRequest;
import com.sound.sound.dto.request.SiteSoundRequest;
import com.sound.sound.dto.response.AudioFileResponse;
import com.sound.sound.dto.response.SiteSoundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SoundController {

    private final SoundService soundService;

    @PostMapping("/audio-file")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAudioFile(@RequestPart(value = "mp3") MultipartFile audioFile,
                                @Valid @RequestPart(value = "email") EmailRequest emailRequest) {
        soundService.uploadAudioFile(audioFile, emailRequest);
    }

    @PostMapping("/site-sound")
    @ResponseStatus(HttpStatus.CREATED)
    public void createSiteSound(@Valid @RequestBody SiteSoundRequest soundRequest) {
        soundService.saveSiteSound(soundRequest);
    }

    @GetMapping("/audio-file")
    @ResponseStatus(HttpStatus.OK)
    public List<AudioFileResponse> readAudioFile(@RequestParam(value = "email") String email) {
        return soundService.queryAudioFile(email);
    }

    @GetMapping("/site-sound")
    @ResponseStatus(HttpStatus.OK)
    public List<SiteSoundResponse> readSiteSound(@RequestParam(value = "email") String email) {
        return soundService.querySiteSound(email);
    }

    @DeleteMapping("/audio-file")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAudioFile(@RequestParam(value = "audioFileId") Integer audioFileId,
                                @RequestParam(value = "email") String email) {
        soundService.deleteAudioFile(audioFileId, email);
    }

    @DeleteMapping("/site-sound")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSiteSound(@RequestParam(value = "siteSoundId") Integer siteSoundId,
                                @RequestParam(value = "audioFileId") Integer audioFileId) {
        soundService.deleteSiteSound(siteSoundId, audioFileId);
    }
}
