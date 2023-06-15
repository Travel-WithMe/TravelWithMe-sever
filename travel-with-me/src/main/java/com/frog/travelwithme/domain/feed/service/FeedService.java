package com.frog.travelwithme.domain.feed.service;

import com.frog.travelwithme.domain.common.like.service.LikeService;
import com.frog.travelwithme.domain.feed.controller.dto.FeedDto;
import com.frog.travelwithme.domain.feed.controller.dto.FeedDto.Response;
import com.frog.travelwithme.domain.feed.entity.Feed;
import com.frog.travelwithme.domain.feed.entity.Tag;
import com.frog.travelwithme.domain.feed.mapper.FeedMapper;
import com.frog.travelwithme.domain.feed.repository.FeedRepository;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ExceptionCode;
import com.frog.travelwithme.global.file.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
public class FeedService implements LikeService {

    private final FeedRepository feedRepository;
    private final MemberService memberService;
    private final TagService tagService;
    private final FeedMapper feedMapper;
    private final FileUploadService fileUploadService;

    public Response postFeed(String email, FeedDto.Post postDto, List<MultipartFile> multipartFiles) {
        Member saveMember = memberService.findMember(email);
        Feed feed = feedMapper.postDtoToFeed(postDto, saveMember);
        this.addTags(postDto.getTags(), feed);
        List<String> addedImageUrls = this.uploadFeedImages(multipartFiles, feed);
        try {
            Feed saveFeed = feedRepository.save(feed);
            return feedMapper.toResponse(saveFeed, email);
        } catch (Exception e) {
            addedImageUrls.forEach(fileUploadService::remove);
            log.debug("FeedService.postFeed exception occur feed = {}", feed.toString());
            throw new BusinessLogicException(ExceptionCode.UNABLE_TO_SAVE_FEED);
        }
    }

    @Transactional(readOnly = true)
    public Feed findFeedById(Long feedId) {
        return this.findFeed(feedId);
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

    @Transactional(readOnly = true)
    public List<Response> findAllByNickname(Long lastFeedId, String nickname, String email) {
        List<Feed> feedList = feedRepository.findAllByNickname(lastFeedId, nickname, email);

        return feedMapper.toResponseList(feedList, email);
    }

    @Transactional(readOnly = true)
    public List<Response> findAllByTagName(Long lastFeedId, String tagName, String email) {
        List<Feed> feedList = feedRepository.findAllByTagName(lastFeedId, tagName, email);

        return feedMapper.toResponseList(feedList, email);
    }

    public Response updateFeed(String email, long feedId, FeedDto.Patch patchDto, List<MultipartFile> multipartFiles) {
        Feed saveFeed = this.findFeed(feedId);
        this.checkWriter(email, saveFeed.getMember().getEmail());
        FeedDto.InternalPatch internalPatchDto = feedMapper.toInternalDto(patchDto);
        saveFeed.updateFeedData(internalPatchDto);
        this.addTags(internalPatchDto.getTags(), saveFeed);
        this.uploadFeedImages(multipartFiles, saveFeed);
        this.removeFeedImages(internalPatchDto.getRemoveImageUrls(), saveFeed);

        return feedMapper.toResponse(saveFeed, email);
    }

    public void deleteFeed(String email, long feedId) {
        Feed saveFeed = this.findFeed(feedId);
        String writerEmail = saveFeed.getMember().getEmail();
        this.checkWriter(email, writerEmail);
        List<String> currentImageUrls = saveFeed.getImageUrls();
        feedRepository.deleteById(feedId);
        currentImageUrls.forEach(fileUploadService::remove);
    }
    // TODO: Redis 캐시 사용 고려

    @Override
    public void doLike(String email, long feedId) {
        Feed feed = this.findFeed(feedId);
        if (!feed.isLikedByMember(email)) {
            feed.addLike(memberService.findMember(email));
        } else {
            log.debug("FeedService.doLike exception occur email : {}, feedId : {}", email, feedId);
            throw new BusinessLogicException(ExceptionCode.ALREADY_LIKED_FEED);
        }
    }
    // TODO: Redis 캐시 사용 고려

    @Override
    public void cancelLike(String email, long feedId) {
        Feed feed = this.findFeed(feedId);
        if (feed.isLikedByMember(email)) {
            feed.removeLike(email);
        } else {
            log.debug("FeedService.cancelLike exception occur email : {}, feedId : {}", email, feedId);
            throw new BusinessLogicException(ExceptionCode.UNABLE_TO_CANCEL_LIKE);
        }
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

    private void addTags(List<String> tags, Feed saveFeed) {
        if (tags != null) {
            Set<Tag> saveTags = tagService.findOrCreateTagsByName(tags);
            saveFeed.addTags(saveTags);
        }
    }

    private List<String> uploadFeedImages(List<MultipartFile> multipartFiles, Feed saveFeed) {
        List<String> addedImageUrls = new ArrayList<>();
        if (multipartFiles != null) {
            multipartFiles.forEach(multipartFile -> {
                String imageUrl = fileUploadService.upload(multipartFile, FEEDIMAGE);
                saveFeed.addImageUrl(imageUrl);
                addedImageUrls.add(imageUrl);
            });
        }

        return addedImageUrls;
    }

    private void removeFeedImages(List<String> removeImageUrls, Feed feed) {
        if (removeImageUrls != null) {
            if (feed.isImageUrlsSizeOne()) {
                log.debug("FeedService.updateFeed exception occur " +
                                "removeImageUrls : {}, saveFeedImageUrls : {}",
                        removeImageUrls,
                        feed.getImageUrls().toString());
                throw new BusinessLogicException(ExceptionCode.UNABLE_TO_DELETE_FEED_IMAGE);
            }
            for (String imageUrl : removeImageUrls) {
                fileUploadService.remove(imageUrl);
                feed.removeImageUrl(imageUrl);
            }
        }
    }
}
