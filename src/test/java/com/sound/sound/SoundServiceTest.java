package com.sound.sound;

import com.sound.sound.dto.request.SiteSoundRequest;
import com.sound.sound.entity.AudioFile;
import com.sound.sound.entity.User;
import com.sound.sound.repository.AudioFileRepository;
import com.sound.sound.repository.SiteSoundRepository;
import com.sound.sound.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SoundApplication.class)
class SoundServiceTest {

    @Autowired
    SoundService soundService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AudioFileRepository audioFileRepository;
    @Autowired
    SiteSoundRepository siteSoundRepository;

    User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .email("test@gmail.com")
                .build());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void saveSiteSound_2urls() {
        //given
        audioFileRepository.save(AudioFile.builder()
                .fileLocation("fileLocation")
                .fileName("fileName")
                .user(user)
                .build());

        SiteSoundRequest siteSoundRequest = new SiteSoundRequest(
                "https://naver.com, \n" +
                        "https://google.com",
                1
        );

        //when
        soundService.saveSiteSound(siteSoundRequest);

        //then
        assertEquals(2, siteSoundRepository.findAll().size());
    }
}