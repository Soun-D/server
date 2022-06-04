package com.sound.sound.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

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
    @Min(value = 1)
    @Max(value = 300)
    private Integer playTime;

    @NotNull
    private Boolean visible;
}
