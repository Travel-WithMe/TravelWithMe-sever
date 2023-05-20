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

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FileUploadService {
    private final AmazonS3ResourceStorage amazonS3ResourceStorage;

    public String store(MultipartFile multipartFile, AwsS3Path awsS3Path) {
        verifiedExenstion(multipartFile);
        return amazonS3ResourceStorage.storeImage(multipartFile, awsS3Path);
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
                || !contentType.contains(IMAGE_JPEG_VALUE)
                || !contentType.contains(IMAGE_PNG_VALUE);
    }
}
