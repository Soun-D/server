package com.sound.sound.repository;

import com.sound.sound.entity.SiteSound;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SiteSoundRepository extends CrudRepository<SiteSound, Integer> {
    boolean existsByUrlAndAudioFile_id(String url, Integer audioFileId);

    List<SiteSound> findAllByAudioFile_id(Integer audioFileId);

    void deleteByIdAndAudioFile_id(Integer id, Integer audioFileId);
}
