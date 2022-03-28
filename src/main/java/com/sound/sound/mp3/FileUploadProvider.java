package com.sound.sound.mp3;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sound.sound.exception.SoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileUploadProvider {

    private final AwsS3UploadService s3UploadService;

    public String uploadFileToS3(MultipartFile file) {
        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            throw new SoundException(400, "잘못된 파일");
        }

        s3UploadService.uploadFile(inputStream, objectMetadata, fileName);
        return s3UploadService.getFileUrl(fileName);
    }

    @Async
    public void deleteFile(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3UploadService.deleteFile(fileName);
    }

    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    public String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new SoundException(400, "StringIndexOutOfBoundsException");
        }
    }

}
