package com.sound.sound.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "url")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Url {
    @EmbeddedId
    private UrlId urlId;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}