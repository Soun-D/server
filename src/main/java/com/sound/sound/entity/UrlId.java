package com.sound.sound.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UrlId implements Serializable {

    private String url;

    @Column(name = "user_id")
    private Integer userId;
}
