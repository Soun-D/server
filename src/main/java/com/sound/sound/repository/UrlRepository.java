package com.sound.sound.repository;

import com.sound.sound.entity.Url;
import com.sound.sound.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UrlRepository extends JpaRepository<Url, Integer> {

    List<Url> findAllByUser(User user);

    boolean existsByUrlAndUser(String url, User user);

    void deleteByUrlAndUser(String url, User user);
}