package com.sound.sound.repository;

import com.sound.sound.entity.AudioFile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AudioFileRepository extends CrudRepository<AudioFile, Integer> {
    List<AudioFile> findAllByUserEmail(String email);

    void deleteByIdAndUserEmail(Integer id, String email);
}
