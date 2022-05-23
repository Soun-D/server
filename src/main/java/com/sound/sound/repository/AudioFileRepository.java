package com.sound.sound.repository;

import com.sound.sound.entity.AudioFile;
import com.sound.sound.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AudioFileRepository extends CrudRepository<AudioFile, Integer> {
    List<AudioFile> findAllByUserEmailAndIsYoutubeOrderById(String email, Boolean isYoutube);

    void deleteByIdAndUserEmail(Integer id, String email);

    boolean existsByFileNameAndUser(String fileName, User user);
}
