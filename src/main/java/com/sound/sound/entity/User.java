package com.sound.sound.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Table(name = "user")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(min = 2)
    @Column(unique = true)
    private String email;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
    private List<AudioFile> audioFileList;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
    private List<Url> UrlList;

    public User(String email) {
        this.email = email;
    }
}
