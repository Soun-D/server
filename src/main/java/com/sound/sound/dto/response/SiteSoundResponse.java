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

    private final String fileLocation;

    private final String fileName;

    private final Integer fileId;

    private final Integer len;

    public static SiteSoundResponse of(SiteSound siteSound) {
        return SiteSoundResponse.builder()
                .id(siteSound.getId())
                .url(siteSound.getUrl())
                .fileLocation(siteSound.getAudioFile().getFileLocation())
                .fileName(siteSound.getAudioFile().getFileName())
                .fileId(siteSound.getAudioFile().getId())
                .len(siteSound.getAudioFile().getLen())
                .build();
    }

    public static SiteSoundResponse oneUrl(String url, SiteSound siteSound) {
        return SiteSoundResponse.builder()
                .url(url)
                .fileLocation(siteSound.getAudioFile().getFileLocation())
                .len(siteSound.getAudioFile().getLen())
                .build();
    }
}
