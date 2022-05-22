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

    private final String fileLocation;

    private final String fileName;

    private final Integer len;

    public static AudioFileResponse of(AudioFile audioFile) {
        return AudioFileResponse.builder()
                .id(audioFile.getId())
                .fileLocation(audioFile.getFileLocation())
                .fileName(audioFile.getFileName())
                .len(audioFile.getLen())
                .build();
    }
}
