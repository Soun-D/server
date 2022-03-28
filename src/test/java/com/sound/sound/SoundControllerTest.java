package com.sound.sound;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sound.sound.dto.request.SiteSoundRequest;
import com.sound.sound.entity.AudioFile;
import com.sound.sound.entity.SiteSound;
import com.sound.sound.entity.User;
import com.sound.sound.mp3.FileUploadProvider;
import com.sound.sound.repository.AudioFileRepository;
import com.sound.sound.repository.SiteSoundRepository;
import com.sound.sound.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class SoundControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AudioFileRepository audioFileRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SiteSoundRepository siteSoundRepository;

    @MockBean
    private FileUploadProvider fileUploadProvider;

    AudioFile audioFile;
    User user;
    SiteSound siteSound;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("test@gmail.com"));
        audioFile = audioFileRepository.save(AudioFile.builder()
                .fileLocation("/index.mp3")
                .user(user)
                .fileName("index")
                .build());
        siteSound = siteSoundRepository.save(SiteSound.builder()
                .audioFile(audioFile)
                .url("url")
                .build());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void createAudioFile() throws Exception {

        // given
        MockMultipartFile mp3File = new MockMultipartFile("mp3",
                "winter-i-miss-yo.mp3",
                "multipart/form-data",
                "D".getBytes(StandardCharsets.UTF_8));

        MockMultipartFile jsonFile = new MockMultipartFile("email",
                "",
                "application/json",
                "{\"email\": \"kwakdh25@gmail.com\"}".getBytes());

        given(fileUploadProvider.uploadFileToS3(any(MultipartFile.class))).willReturn("url");

        // when
        mvc.perform(multipart("/audio-file")
                .file(mp3File)
                .file(jsonFile)
        .contentType(MediaType.MULTIPART_FORM_DATA))

        // then
        .andExpect(status().isCreated());

        List<AudioFile> audioFileList = audioFileRepository.findAllByUserEmail("kwakdh25@gmail.com");

        audioFileList.forEach(audioFile1 ->
                assertEquals("url", audioFile1.getFileLocation())
        );
    }

    @Test
    void createSiteSound() throws Exception {
        // given
        mvc.perform(post("/site-sound")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SiteSoundRequest("https://naver.com", audioFile.getId()))))

        // then
                .andExpect(status().isCreated());

    }

    @Test
    void readAudioFile() throws Exception {
        // given
        mvc.perform(get("/audio-file")
                .param("email", user.getEmail()))

                // then
                .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].file_location").value("/index.mp3"));
    }

    @Test
    void readSiteSound() throws Exception {
        // given
        mvc.perform(get("/site-sound")
                .param("audioFileId", audioFile.getId().toString()))

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].url").value("url"));
    }

    @Test
    void deleteAudioFile() throws Exception {
        // given
        mvc.perform(delete("/audio-file")
                .param("audioFileId", audioFile.getId().toString())
                .param("email", user.getEmail()))

                // then
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteSiteSound() throws Exception {
        // given
        mvc.perform(delete("/site-sound")
                .param("audioFileId", audioFile.getId().toString())
                .param("siteSoundId", siteSound.getId().toString()))

                // then
                .andExpect(status().isNoContent());
    }
}