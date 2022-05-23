package com.sound.sound;

import com.sound.sound.dto.request.SiteSoundRequest;
import com.sound.sound.entity.AudioFile;
import com.sound.sound.entity.User;
import com.sound.sound.repository.AudioFileRepository;
import com.sound.sound.repository.SiteSoundRepository;
import com.sound.sound.repository.UrlRepository;
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
    @Autowired
    UrlRepository urlRepository;

    User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .email("kwakdh25@gmail.com")
                .build());
    }

    @AfterEach
    void tearDown() {
        siteSoundRepository.deleteAll();
        audioFileRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void saveSiteSound_2urls() {
        //given
        AudioFile audioFile = audioFileRepository.save(AudioFile.builder()
                .src("fileLocation")
                .title("fileName")
                .user(user)
                .isYoutube(false)
                .playTime(1)
                .build());

        SiteSoundRequest siteSoundRequest = new SiteSoundRequest(
                "https://naver.com, \n" +
                        "https://google.com",
                audioFile.getId()
        );

        //when
        soundService.saveSiteSound(siteSoundRequest);

        //then
        assertEquals(1, siteSoundRepository.findAll().size());
        assertEquals(2, urlRepository.findAll().size());
    }
}