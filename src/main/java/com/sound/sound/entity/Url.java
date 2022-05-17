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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 2000)
    private String url;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}