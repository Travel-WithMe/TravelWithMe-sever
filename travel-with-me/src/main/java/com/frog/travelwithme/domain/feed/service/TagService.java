package com.frog.travelwithme.domain.feed.service;

import com.frog.travelwithme.domain.feed.controller.dto.TagDto;
import com.frog.travelwithme.domain.feed.entity.Tag;
import com.frog.travelwithme.domain.feed.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public List<TagDto.Response> findTagsStartingWith(String tagName, int size) {
        log.info("TagService tagName = {}", tagName);
        return tagRepository.findTagsStartingWith(tagName, size);
    }

    public Tag findOrCreateTag(String tagName) {
        return tagRepository.findByName(tagName).orElseGet(() -> {
            Tag tag = Tag.builder().name(tagName).build();
            return tagRepository.save(tag);
        });
    }
}
