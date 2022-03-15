package com.sound.sound.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SiteSoundRequest {

    @NotBlank
    private String url;

    @NotNull
    private Integer audioFileId;
}
