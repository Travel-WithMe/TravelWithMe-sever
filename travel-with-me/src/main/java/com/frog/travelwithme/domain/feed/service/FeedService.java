package com.frog.travelwithme.domain.feed.service;

import com.frog.travelwithme.domain.feed.controller.dto.FeedDto;
import com.frog.travelwithme.domain.feed.controller.dto.FeedDto.Response;
import com.frog.travelwithme.domain.feed.entity.Feed;
import com.frog.travelwithme.domain.feed.entity.Tag;
import com.frog.travelwithme.domain.feed.mapper.FeedMapper;
import com.frog.travelwithme.domain.feed.repository.FeedRepository;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.global.enums.EnumCollection.ResponseBody;
import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ExceptionCode;
import com.frog.travelwithme.global.file.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.frog.travelwithme.global.enums.EnumCollection.AwsS3Path.FEEDIMAGE;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/17
 **/
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final MemberService memberService;
    private final TagService tagService;
    private final FeedMapper feedMapper;
    private final FileUploadService fileUploadService;

    public Response postFeed(String email, FeedDto.Post postDto, List<MultipartFile> multipartFiles) {
        Member saveMember = memberService.findMember(email);
        List<String> imageUrls = multipartFiles.stream()
                .map(multipartFile -> fileUploadService.upload(multipartFile, FEEDIMAGE)).collect(Collectors.toList());
        Feed feed = feedMapper.postDtoToFeed(postDto, saveMember, imageUrls);
        if (postDto.getTags() != null) {
            Set<Tag> tags = tagService.findOrCreateTagsByName(postDto.getTags());
            feed.addTags(tags);
        }
        Feed saveFeed = feedRepository.save(feed);

        return feedMapper.toResponse(saveFeed, email);
    }

    @Transactional(readOnly = true)
    public Response findFeedById(String email, long feedId) {
        return feedMapper.toResponse(this.findFeed(feedId), email);
    }

    @Transactional(readOnly = true)
    public List<Response> findAll(Long lastFeedId, String email) {
        List<Feed> feedList = feedRepository.findAll(lastFeedId, email);

        return feedMapper.toResponseList(feedList, email);
    }

    public Response updateFeed(String email, long feedId, FeedDto.Patch patchDto) {
        Feed saveFeed = this.findFeed(feedId);
        String writerEmail = saveFeed.getMember().getEmail();
        this.checkWriter(email, writerEmail);
        FeedDto.InternalPatch internalPatchDto = feedMapper.toInternalDto(patchDto);
        saveFeed.updateFeedData(internalPatchDto);
        if (internalPatchDto.getTags() != null) {
            Set<Tag> tags = tagService.findOrCreateTagsByName(internalPatchDto.getTags());
            saveFeed.addTags(tags);
        }

        return feedMapper.toResponse(saveFeed, email);
    }

    public void deleteFeed(String email, long feedId) {
        Feed saveFeed = this.findFeed(feedId);
        String writerEmail = saveFeed.getMember().getEmail();
        checkWriter(email, writerEmail);
        feedRepository.deleteById(feedId);
    }

    // TODO: Redis 캐시 사용 고려
    public ResponseBody doLike(String email, long feedId) {
        Feed feed = this.findFeed(feedId);
        if (!feed.isLikedByMember(email)) {
            feed.addLike(memberService.findMember(email));
        } else {
            log.debug("FeedService.doLike exception occur email : {}, feedId : {}", email, feedId);
            throw new BusinessLogicException(ExceptionCode.ALREADY_LIKED_FEED);
        }
        return ResponseBody.SUCCESS_FEED_LIKE;
    }

    // TODO: Redis 캐시 사용 고려
    public ResponseBody cancelLike(String email, long feedId) {
        Feed feed = this.findFeed(feedId);
        if (feed.isLikedByMember(email)) {
            feed.removeLike(email);
        } else {
            log.debug("FeedService.cancelLike exception occur email : {}, feedId : {}", email, feedId);
            throw new BusinessLogicException(ExceptionCode.UNABLE_TO_CANCEL_LIKE);
        }
        return ResponseBody.SUCCESS_CANCEL_FEED_LIKE;
    }

    private void checkWriter(String email, String writerEmail) {
        if (!email.equals(writerEmail)) {
            log.debug("FeedService.checkWriter exception occur email : {}, writerEmail : {}",
                    email, writerEmail);
            throw new BusinessLogicException(ExceptionCode.FEED_WRITER_NOT_MATCH);
        }
    }

    private Feed findFeed(Long feedId) {
        return feedRepository.findById(feedId)
                .orElseThrow(() -> {
                    log.debug("FeedService.findFeed exception occur feedId : {}", feedId);
                    throw new BusinessLogicException(ExceptionCode.FEED_NOT_FOUND);
                });
    }
}
