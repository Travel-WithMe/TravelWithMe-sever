package com.frog.travelwithme.unit.file;

import com.frog.travelwithme.global.enums.EnumCollection.AwsS3Path;
import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ExceptionCode;
import com.frog.travelwithme.global.file.AmazonS3ResourceStorage;
import com.frog.travelwithme.global.file.FileUploadService;
import com.frog.travelwithme.utils.StubData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/05/21
 **/
@ExtendWith(MockitoExtension.class)
class FileUploadServiceTest {

    @InjectMocks
    private FileUploadService fileUploadService;

    @Mock
    private AmazonS3ResourceStorage amazonS3ResourceStorage;

    @Test
    @DisplayName("파일 업로드 시 S3에 저장된 url 반환")
    void fileUploadServiceTest1() throws Exception {
        // given
        MockMultipartFile file = StubData.CustomMockMultipartFile.getFile();
        AwsS3Path feedimage = AwsS3Path.FEEDIMAGE;
        String fileUrl = "fileUrl";
        given(amazonS3ResourceStorage.uploadImage(any(MultipartFile.class), any(AwsS3Path.class))).willReturn(fileUrl);

        // when
        String actualFileUrl = amazonS3ResourceStorage.uploadImage(file, feedimage);

        // then
        assertThat(actualFileUrl).isEqualTo(fileUrl);
    }

    @Test
    @DisplayName("확장자명이 jpg/png가 아니라면 예외 발생")
    void fileUploadServiceTest2() throws Exception {
        // given
        MockMultipartFile failFile = StubData.CustomMockMultipartFile.getFailFile();
        AwsS3Path feedimage = AwsS3Path.FEEDIMAGE;

        //when // then
        assertThatThrownBy(() -> fileUploadService.upload(failFile, feedimage))
                .isInstanceOf(BusinessLogicException.class)
                .hasMessage(ExceptionCode.EXTENSION_IS_NOT_VALID.getMessage());
    }
}
