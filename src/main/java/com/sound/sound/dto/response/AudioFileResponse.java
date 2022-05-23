package com.sound.sound.dto.response;

import com.sound.sound.entity.AudioFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@AllArgsConstructor
@Builder
public class AudioFileResponse {
    private final Integer id;

    private final String src;

    private final String title;

    private final Integer playTime;

    private final Boolean isYoutube;

    public static AudioFileResponse of(AudioFile audioFile) {
        return AudioFileResponse.builder()
                .id(audioFile.getId())
                .src(audioFile.getSrc())
                .title(audioFile.getTitle())
                .playTime(audioFile.getPlayTime())
                .isYoutube(audioFile.getIsYoutube())
                .build();
    }
}

