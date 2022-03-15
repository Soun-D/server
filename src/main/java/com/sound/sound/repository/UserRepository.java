package com.sound.sound.repository;

import com.sound.sound.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);

    boolean existsByEmail(String email);
}
