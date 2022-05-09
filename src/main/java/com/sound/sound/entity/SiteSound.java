package com.sound.sound.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SiteSound {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String url;

    @ManyToOne
    private AudioFile audioFile;

    public SiteSound update(String url, AudioFile audioFile) {
        this.url = url;
        this.audioFile = audioFile;
        return this;
    }
}
