package com.sound.sound.repository;

import com.sound.sound.entity.SiteSound;
import org.springframework.data.repository.CrudRepository;

public interface SiteSoundRepository extends CrudRepository<SiteSound, Integer> {
    boolean existsByUrlAndAudioFile_id(String url, Integer audioFileId);
}
