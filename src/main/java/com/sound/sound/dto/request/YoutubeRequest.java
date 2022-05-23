package com.sound.sound.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class YoutubeRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String iframe;

    @NotBlank
    private String title;

    @NotNull
    private Integer len;
}
