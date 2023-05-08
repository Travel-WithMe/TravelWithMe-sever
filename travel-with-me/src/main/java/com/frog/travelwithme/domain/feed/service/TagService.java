package com.frog.travelwithme.domain.feed.service;

import com.frog.travelwithme.domain.feed.controller.dto.TagDto;
import com.frog.travelwithme.domain.feed.entity.Tag;
import com.frog.travelwithme.domain.feed.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    public List<TagDto.Response> findTagsStartWith(String tagName, int size) {
        return tagRepository.findTagsStartWith(tagName, size);
    }

    public Set<Tag> findOrCreateTagsByName(List<String> tagNames) {
        return tagNames.stream()
                .map(this::findOrCreateTag)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Tag findOrCreateTag(String tagName) {
        return tagRepository.findByName(tagName)
                .orElseGet(() -> this.createTag(tagName)
        );
    }

    private Tag createTag(String tagNae) {
        Tag tag = Tag.builder().name(tagNae).build();
        return tagRepository.save(tag);
    }
}
