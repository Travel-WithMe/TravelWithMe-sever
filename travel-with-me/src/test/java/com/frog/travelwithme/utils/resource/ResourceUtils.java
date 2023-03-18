package com.frog.travelwithme.utils.resource;

import org.springframework.util.MultiValueMap;

public interface ResourceUtils {

    MultiValueMap<String, String> getParams(String key1, String value1, String key2, String value2);

    String getResourceUrl();

    String getResourceUrl(Long id);

    String getResourceUrl(Long id, String url);

}
