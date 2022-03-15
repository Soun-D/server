package com.sound.sound.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SiteSoundRequest {

    @NotBlank
    private String url;

    @NotBlank
    private Integer audioFileId;
}
