package com.sound.sound.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SiteSoundUpdateRequest {

    @NotNull
    private Integer id;

    @NotBlank
    private String url;

    @NotNull
    private Integer audioFileId;
}
