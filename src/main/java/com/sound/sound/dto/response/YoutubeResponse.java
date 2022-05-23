package com.sound.sound.dto.response;

import com.sound.sound.dto.request.YoutubeRequest;
import com.sound.sound.entity.AudioFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class YoutubeResponse {

    private final Integer id;

    private final String iframe;

    private final String title;

    private final Integer len;

    public static YoutubeResponse of(AudioFile audioFile) {
        return YoutubeResponse.builder()
                .id(audioFile.getId())
                .iframe(audioFile.getFileLocation())
                .title(audioFile.getFileName())
                .len(audioFile.getLen())
                .build();
    }
}