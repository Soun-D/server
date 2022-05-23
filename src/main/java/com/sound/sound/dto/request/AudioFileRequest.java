package com.sound.sound.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class AudioFileRequest {

    @Email
    @NotBlank
    private String email;

    @NotNull
    private Integer playTime;
}
