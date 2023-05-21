package com.frog.travelwithme.unit.file;

import com.amazonaws.services.s3.AmazonS3;
import com.frog.travelwithme.global.enums.EnumCollection;
import com.frog.travelwithme.global.file.AmazonS3ResourceStorage;
import com.frog.travelwithme.utils.StubData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/05/21
 **/
@ExtendWith(MockitoExtension.class)
public class AmazonS3ResourceStorageTest {

    @InjectMocks
    private AmazonS3ResourceStorage amazonS3ResourceStorage;

    @Mock
    private AmazonS3 amazonS3;

    @Test
    @DisplayName("S3 파일 업로드")
    void AmazonS3ResourceStorageTest1() throws Exception {
        // given
        URL url = new URL("http://example.com");
        String fileUrl = url.toString();
        MockMultipartFile file = StubData.CustomMockMultipartFile.getFile();
        EnumCollection.AwsS3Path feedimage = EnumCollection.AwsS3Path.FEEDIMAGE;
        given(amazonS3.getUrl(any(), any())).willReturn(url);

        // when
        String actualFileUrl = amazonS3ResourceStorage.uploadImage(file, feedimage);

        // then
        assertThat(actualFileUrl).isEqualTo(fileUrl);
    }
}
