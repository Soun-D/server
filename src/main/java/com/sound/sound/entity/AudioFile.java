package com.sound.sound.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AudioFile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(unique = true)
    private String fileLocation;

    @NotNull
    @Column(unique = true)
    private String fileName;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "audioFile")
    private List<SiteSound> siteSound;
}
