package com.sound.sound.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class YoutubeUpdateRequest {

    @NotNull
    private Integer id;

    @NotBlank
    private String title;

    @NotNull
    private Boolean visible;

    @NotNull
    private Integer playTime;
}
