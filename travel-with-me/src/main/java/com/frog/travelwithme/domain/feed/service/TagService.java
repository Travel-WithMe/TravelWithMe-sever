package com.frog.travelwithme.domain.feed.service;

import com.frog.travelwithme.domain.feed.controller.dto.TagDto;
import com.frog.travelwithme.domain.feed.entity.Feed;
import com.frog.travelwithme.domain.feed.entity.FeedTag;
import com.frog.travelwithme.domain.feed.entity.Tag;
import com.frog.travelwithme.domain.feed.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    public List<TagDto.Response> findTagsStartingWith(String tagName, int size) {
        return tagRepository.findTagsStartingWith(tagName, size);
    }

    public List<FeedTag> createFeedTags(Feed feed, List<String> tagNames) {
        return this.convertTagNamesToTags(tagNames).stream()
                .map(tag -> FeedTag.builder()
                        .tag(tag)
                        .feed(feed)
                        .name(tag.getName())
                        .build())
                .collect(Collectors.toList());
    }

    private List<Tag> convertTagNamesToTags(List<String> tagNames) {
        return tagNames.stream()
                .map(this::findOrCreateTagByName)
                .collect(Collectors.toList());
    }

    private Tag findOrCreateTagByName(String tagName) {
        return tagRepository.findByName(tagName)
                .orElseGet(() -> this.createTag(tagName)
        );
    }

    private Tag createTag(String tagNae) {
        Tag tag = Tag.builder().name(tagNae).build();
        return tagRepository.save(tag);
    }
}
