package com.sound.sound.repository;

import com.sound.sound.entity.AudioFile;
import org.springframework.data.repository.CrudRepository;

public interface AudioFileRepository extends CrudRepository<AudioFile, Integer> {
}
