package com.frog.travelwithme.global.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
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

/**
 * AmazonS3ResourceStorage 설명: S3 파일 업로드 및 삭제 로직 구현
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/05/20
 **/
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AmazonS3ResourceStorage {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadImage(MultipartFile image, EnumCollection.AwsS3Path awsS3Path) {
        if (image.isEmpty()) {
            log.debug("FileService.storeImage exception occur image : {}, awsS3Path : {}",
                    image, awsS3Path);
            throw new BusinessLogicException(ExceptionCode.FILE_DOES_NOT_EXIST);
        }

        String originalFilename = image.getOriginalFilename();
        String storeFileName = this.createStoreFileName(originalFilename);

        try (InputStream inputStream = image.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(
                    bucketName + awsS3Path.getPath(), storeFileName, inputStream, null));
        } catch (IOException e) {
            log.debug("FileService.storeImage exception occur image : {}, awsS3Path : {}",
                    image, awsS3Path);
            throw new BusinessLogicException(ExceptionCode.FAIL_TO_UPLOAD_FILE);
        }

        return amazonS3.getUrl(bucketName + awsS3Path.getPath(), storeFileName).toString();
    }

    public void removeImage(String imageUrl) {
        try {
            String key = imageUrl.substring(64);
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        } catch (Exception e) {
            log.debug("AmazonS3ResourceStorage.deleteImage exception occur imageUrl : {}", imageUrl);
            throw new BusinessLogicException(ExceptionCode.FAILED_TO_DELETE_FILE);
        }
    }

    private String createStoreFileName(String originalFilename) {
        return UUID.randomUUID() + "." + extractExt(originalFilename);
    }

    private String extractExt(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }
}
