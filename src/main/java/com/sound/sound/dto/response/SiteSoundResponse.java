package com.sound.sound.dto.response;

import com.sound.sound.entity.SiteSound;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SiteSoundResponse {
    private final Integer id;

    private final String url;

    private final String src;

    private final String title;

    private final Integer fileId;

    private final Integer playTime;

    public static SiteSoundResponse of(SiteSound siteSound) {
        return SiteSoundResponse.builder()
                .id(siteSound.getId())
                .url(siteSound.getUrl())
                .src(siteSound.getAudioFile().getSrc())
                .title(siteSound.getAudioFile().getTitle())
                .fileId(siteSound.getAudioFile().getId())
                .playTime(siteSound.getAudioFile().getPlayTime())
                .build();
    }

    public static SiteSoundResponse oneUrl(String url, SiteSound siteSound) {
        return SiteSoundResponse.builder()
                .url(url)
                .src(siteSound.getAudioFile().getSrc())
                .playTime(siteSound.getAudioFile().getPlayTime())
                .build();
    }
}
