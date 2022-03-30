package com.sound.sound.repository;

import com.sound.sound.entity.SiteSound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SiteSoundRepository extends JpaRepository<SiteSound, Integer> {
    boolean existsByUrlAndAudioFile_id(String url, Integer audioFileId);

    List<SiteSound> findAllByAudioFileUserEmail(String email);

    void deleteByIdAndAudioFile_id(Integer id, Integer audioFileId);
}
