package com.sound.sound;

import com.sound.sound.dto.request.*;
import com.sound.sound.dto.response.AudioFileResponse;
import com.sound.sound.dto.response.SiteSoundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static com.sound.sound.dto.response.AudioFileResponse.*;

@RestController
@RequiredArgsConstructor
public class SoundController {

    private final SoundService soundService;

    @PostMapping("/audio-file")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAudioFile(@RequestPart(value = "mp3") MultipartFile audioFile,
                                @Valid @RequestPart(value = "email") AudioFileRequest audioFileRequest) {
        soundService.uploadAudioFile(audioFile, audioFileRequest);
    }

    @PostMapping("/site-sound")
    @ResponseStatus(HttpStatus.CREATED)
    public void createSiteSound(@Valid @RequestBody SiteSoundRequest soundRequest) {
        soundService.saveSiteSound(soundRequest);
    }

    @PostMapping("/youtube")
    @ResponseStatus(HttpStatus.CREATED)
    public void createYoutube(@Valid @RequestBody YoutubeRequest youtubeRequest) {
        soundService.saveYoutube(youtubeRequest);
    }

    @PutMapping("/site-sound")
    @ResponseStatus(HttpStatus.CREATED)
    public void updateSiteSound(@Valid @RequestBody SiteSoundUpdateRequest soundRequest) {
        soundService.updateSiteSound(soundRequest);
    }

    @GetMapping("/audio-file/all")
    @ResponseStatus(HttpStatus.OK)
    public List<AudioFileResponse> readAllAudioFile(@RequestParam(value = "email") String email) {
        return soundService.queryAllAudioFile(email);
    }

    @GetMapping("/audio-file")
    @ResponseStatus(HttpStatus.OK)
    public List<AudioFileResponse> readAudioFile(@RequestParam(value = "email") String email) {
        return soundService.queryAudioFile(email);
    }

    @GetMapping("/youtube")
    @ResponseStatus(HttpStatus.OK)
    public List<AudioFileResponse> readYoutube(@RequestParam(value = "email") String email) {
        return soundService.queryYoutube(email);
    }

    @GetMapping("/site-sound")
    @ResponseStatus(HttpStatus.OK)
    public List<SiteSoundResponse> readSiteSound(@RequestParam(value = "email") String email) {
        return soundService.querySiteSound(email);
    }

    @GetMapping("/site-sound/split")
    @ResponseStatus(HttpStatus.OK)
    public List<SiteSoundResponse> readSplitSiteSound(@RequestParam(value = "email") String email) {
        return soundService.querySplitSiteSound(email);
    }

    @DeleteMapping("/audio-file")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAudioFile(@RequestBody IdReq audioFileId,
                                @RequestParam(value = "email") String email) {
        soundService.deleteAudioFile(audioFileId.getId(), email);
    }

    @DeleteMapping("/site-sound")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSiteSound(@RequestBody IdReq siteSoundId) {
        soundService.deleteSiteSound(siteSoundId.getId());
    }
}
