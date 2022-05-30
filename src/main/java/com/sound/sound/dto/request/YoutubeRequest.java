package com.sound.sound.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class YoutubeRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String src;

    @NotBlank
    private String title;

    @NotNull
    @Size(min = 1, max = 300)
    private Integer playTime;

    @NotNull
    private Boolean visible;
}
