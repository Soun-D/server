package com.sound.sound.entity;

import com.sound.sound.dto.request.YoutubeUpdateRequest;
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
    @Column(unique = true, nullable = false)
    private String src;

    @NotNull
    @Column(unique = true, nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "play_time", nullable = false)
    private Integer playTime;

    @Column(name = "is_youtube", nullable = false, columnDefinition = "tinyint(1) default 0")
    private Boolean isYoutube;

    @Column(name = "visible", nullable = false, columnDefinition = "tinyint(1) default 0")
    private Boolean visible;

    @Column(name = "start")
    private String start;

    @OneToMany(mappedBy = "audioFile")
    private List<SiteSound> siteSound;

    public AudioFile update(YoutubeUpdateRequest request) {
        this.title = request.getTitle();
        this.playTime = request.getPlayTime();
        this.visible = request.getVisible();
        return this;
    }
}
