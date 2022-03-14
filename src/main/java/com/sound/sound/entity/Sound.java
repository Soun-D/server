package com.sound.sound.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Sound {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private String email;

    @NotNull
    private String mp3;

    @NotNull
    private String label;
}
