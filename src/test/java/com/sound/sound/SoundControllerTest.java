package com.sound.sound;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sound.sound.dto.request.IdReq;
import com.sound.sound.dto.request.SiteSoundRequest;
import com.sound.sound.dto.request.SiteSoundUpdateRequest;
import com.sound.sound.entity.*;
import com.sound.sound.mp3.FileUploadProvider;
import com.sound.sound.repository.AudioFileRepository;
import com.sound.sound.repository.SiteSoundRepository;
import com.sound.sound.repository.UrlRepository;
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
    @Autowired
    UrlRepository urlRepository;

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
                .len(1)
                .build());
        siteSound = siteSoundRepository.save(SiteSound.builder()
                .audioFile(audioFile)
                .url("https://test.com")
                .build());
        urlRepository.save(Url.builder()
                .user(user)
                .url("https://test.com")
                .build());
    }

    @AfterEach
    void tearDown() {
        siteSoundRepository.deleteAll();
        audioFileRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void AudioFile_저장_201() throws Exception {

        // given
        MockMultipartFile mp3File = new MockMultipartFile("mp3",
                "winter-i-miss-yo.mp3",
                "multipart/form-data",
                "D".getBytes(StandardCharsets.UTF_8));

        MockMultipartFile jsonFile = new MockMultipartFile("email",
                "",
                "application/json",
                "{\"email\": \"kwakdh25@gmail.com\", \"len\": 1}".getBytes());

        given(fileUploadProvider.uploadFileToS3(any(MultipartFile.class))).willReturn("https://test.com");

        // when
        mvc.perform(multipart("/audio-file")
                .file(mp3File)
                .file(jsonFile)
        .contentType(MediaType.MULTIPART_FORM_DATA))

        // then
        .andExpect(status().isCreated());

        List<AudioFile> audioFileList = audioFileRepository.findAllByUserEmailOrderById("kwakdh25@gmail.com");

        audioFileList.forEach(audioFile1 ->
                assertEquals("https://test.com", audioFile1.getFileLocation())
        );
    }

    @Test
    void SiteSound_저장_201() throws Exception {
        // given
        mvc.perform(post("/site-sound")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SiteSoundRequest("https://google.com,   http://knowing.com", audioFile.getId()))))

        // then
                .andExpect(status().isCreated());

        assertEquals(3, urlRepository.findAll().size());
        assertEquals(2, siteSoundRepository.findAll().size());
    }

    @Test
    void SiteSound_저장_400_유효한_URL_형식이_아닙니다() throws Exception {
        // given
        mvc.perform(post("/site-sound")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SiteSoundRequest("https://naver.com, " +
                        "https://google.com,   http://.com", audioFile.getId()))))

                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    void SiteSound_저장_409_URL_중복() throws Exception {
        // given
        mvc.perform(post("/site-sound")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SiteSoundRequest("https://naver.com, " +
                        "https://naver.com, https://test.com", audioFile.getId()))))

                // then
                .andExpect(status().isConflict());
    }

    @Test
    void SiteSound_수정_201() throws Exception {
        // given
        String url = "https://naver.com, " +
                "https://facebook.com, https://test.com";
        mvc.perform(put("/site-sound")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SiteSoundUpdateRequest(siteSound.getId(), url, audioFile.getId()))))

                // then
                .andExpect(status().isCreated());

        assertEquals(3, urlRepository.findAllByUser(user).size());
        assertEquals(siteSoundRepository.findById(siteSound.getId()).orElseThrow().getUrl(), url);

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
                .param("email", user.getEmail()))

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].url").value("https://test.com"));
    }

    @Test
    void deleteAudioFile_400() throws Exception {
        // given
        mvc.perform(delete("/audio-file")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new IdReq(audioFile.getId())))
                .param("email", user.getEmail()))

                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteSiteSound() throws Exception {
        // given
        mvc.perform(delete("/site-sound")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new IdReq(siteSound.getId()))))

                // then
                .andExpect(status().isNoContent());
    }
}