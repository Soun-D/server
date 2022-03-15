package com.sound.sound.dto.response;

import com.sound.sound.entity.AudioFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AudioFileResponse {

    private Integer id;

    private String fileLocation;

    private String fileName;

    public static AudioFileResponse of(AudioFile audioFile) {
        return AudioFileResponse.builder()
                .id(audioFile.getId())
                .fileLocation(audioFile.getFileLocation())
                .fileName(audioFile.getFileName())
                .build();
    }
}
