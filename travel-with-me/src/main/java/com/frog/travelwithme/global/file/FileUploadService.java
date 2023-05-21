package com.frog.travelwithme.global.file;

import com.frog.travelwithme.global.enums.EnumCollection.AwsS3Path;
import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

/**
 * FileUploadService 설명: 파일 유효성 검사 및 업로드,삭제 요청
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/05/20
 **/
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FileUploadService {
    private final AmazonS3ResourceStorage amazonS3ResourceStorage;

    public String upload(MultipartFile multipartFile, AwsS3Path awsS3Path) {
        verifiedExenstion(multipartFile);
        return amazonS3ResourceStorage.uploadImage(multipartFile, awsS3Path);
    }

    public void remove(String imageUrl) {
        amazonS3ResourceStorage.removeImage(imageUrl);
    }

    private void verifiedExenstion(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();

        if (isNotJpegOrPngByContentType(contentType)) {
            log.debug("FileUploadService.verifiedExenstion exception occur contentType : {}",
                    multipartFile.getContentType());
            throw new BusinessLogicException(ExceptionCode.EXTENSION_IS_NOT_VALID);
        }
    }

    private boolean isNotJpegOrPngByContentType(String contentType) {
        return ObjectUtils.isEmpty(contentType)
                || (!contentType.contains(IMAGE_JPEG_VALUE) && !contentType.contains(IMAGE_PNG_VALUE));
    }
}
