package com.frog.travelwithme.utils.resource;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/06
 **/
public class AuthResourceUtils implements ResourceUtils {
    private static final String PATH = "/";
    private static final String BASE_URL = "/auth";
    private static final String RESOURCE_ID = "/{member-id}";

    @Override
    public MultiValueMap<String, String> getParams(String key1, String value1, String key2, String value2) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(key1, value1);
        params.add(key2, value2);
        return params;
    }

    @Override
    public String getResourceUrl() {
        return UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();
    }

    @Override
    public String getResourceUrl(String url) {
        return UriComponentsBuilder.newInstance().path(BASE_URL + PATH + url)
                .build().toUri().toString();
    }

    @Override
    public String getResourceUrl(Long id) {
        return UriComponentsBuilder.newInstance().path(BASE_URL + RESOURCE_ID)
                .buildAndExpand(id).toUri().toString();
    }

    @Override
    public String getResourceUrl(Long id, String url) {
        return UriComponentsBuilder.newInstance().path(BASE_URL + RESOURCE_ID + PATH + url)
                .buildAndExpand(id).toUri().toString();
    }
}
