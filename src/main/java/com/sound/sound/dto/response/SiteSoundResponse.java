package com.sound.sound.dto.response;

import com.sound.sound.entity.SiteSound;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SiteSoundResponse {
    private Integer id;

    private String url;

    public static SiteSoundResponse of(SiteSound siteSound) {
        return SiteSoundResponse.builder()
                .id(siteSound.getId())
                .url(siteSound.getUrl())
                .build();
    }
}
