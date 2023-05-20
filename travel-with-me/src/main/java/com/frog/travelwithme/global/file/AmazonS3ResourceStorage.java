package com.frog.travelwithme.global.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.frog.travelwithme.global.enums.EnumCollection;
import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AmazonS3ResourceStorage {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String storeImage(MultipartFile image, EnumCollection.AwsS3Path awsS3Path) {
        if (image.isEmpty()) {
            log.debug("FileService.storeImage exception occur image : {}, awsS3Path : {}",
                    image, awsS3Path);
            throw new BusinessLogicException(ExceptionCode.FILE_DOES_NOT_EXIST);
        }

        String originalFilename = image.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        try (InputStream inputStream = image.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(
                    bucketName + awsS3Path.getPath(), storeFileName, inputStream, null));
        } catch (IOException e) {
            log.debug("FileService.storeImage exception occur image : {}, awsS3Path : {}",
                    image, awsS3Path);
            throw new BusinessLogicException(ExceptionCode.FAIL_TO_UPLOAD_FILE);
        }

        return amazonS3Client.getUrl(bucketName + awsS3Path.getPath(), storeFileName).toString();
    }

    private String createStoreFileName(String originalFilename) {
        return UUID.randomUUID() + "." + extractExt(originalFilename);
    }

    private String extractExt(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }
}
