package com.frog.travelwithme.domain.feed.service;

import com.frog.travelwithme.domain.common.comment.dto.CommentDto;
import com.frog.travelwithme.domain.common.comment.dto.CommentTypeDto;
import com.frog.travelwithme.domain.common.comment.service.CommentService;
import com.frog.travelwithme.domain.feed.entity.Feed;
import com.frog.travelwithme.domain.feed.entity.FeedComment;
import com.frog.travelwithme.domain.feed.mapper.FeedCommentMapper;
import com.frog.travelwithme.domain.feed.repository.FeedCommentRepository;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.global.enums.EnumCollection.Comment;
import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/07/03
 **/
@Slf4j
@Service
@Transactional
public class FeedCommentService extends CommentService {

    private final FeedCommentRepository feedCommentRepository;

    private final MemberService memberService;

    private final FeedService feedService;

    private final FeedCommentMapper feedCommentMapper;

    public FeedCommentService(MemberService memberService,
                              FeedCommentRepository feedCommentRepository,
                              MemberService memberService1,
                              FeedService feedService,
                              FeedCommentMapper feedCommentMapper) {
        super(memberService);
        this.feedCommentRepository = feedCommentRepository;
        this.memberService = memberService1;
        this.feedService = feedService;
        this.feedCommentMapper = feedCommentMapper;
    }

    public CommentDto.PostResponse createCommentByEmail(CommentDto.Post postDto,
                                                        Long feedId,
                                                        String email) {
        super.checkExistTaggedMemberId(postDto);
        super.checkPossibleToMakeGroup(postDto);    // 메서드명 기능에 맞게 변경 고려
        Member member = memberService.findMember(email);
        Feed feed = feedService.findFeed(feedId);
        FeedComment feedComment = feedCommentRepository.save(
                feedCommentMapper.toEntity(postDto, member, feed));
        this.joinGroup(feedComment);

        if (feedComment.hasTaggedMember()) {
            String nickname = memberService.findMember(feedComment.getTaggedMemberId()).getNickname();
            return feedCommentMapper.toPostResponseDto(feedComment, nickname);
        } else {
            return feedCommentMapper.toPostResponseDto(feedComment);
        }
    }

    public CommentDto.PatchResponse updateCommentByEmail(CommentDto.Patch patchDto,
                                                        Long commentId,
                                                        String email) {
        FeedComment feedComment = this.findFeedCommentById(commentId);
        this.checkEqualWriterAndUser(feedComment, email);
        this.checkDeletedComment(feedComment);
        feedComment.updateFeedComment(patchDto);

        if (feedComment.hasTaggedMember()) {
            String nickname = memberService.findMember(feedComment.getTaggedMemberId()).getNickname();
            return feedCommentMapper.toPatchResponseDto(feedComment, nickname);
        } else {
            return feedCommentMapper.toPatchResponseDto(feedComment);
        }
    }

    public CommentDto.DeleteResponse deleteCommentByEmail(Long commentId, String email) {
        FeedComment feedComment = this.findFeedCommentById(commentId);
        this.checkEqualWriterAndUser(feedComment, email);
        this.checkDeletedComment(feedComment);
        feedComment.softDeleteComment();

        return feedCommentMapper.toDelteResponseDto(feedComment,
                Comment.DELETE.getDescription());
    }

    private void checkEqualWriterAndUser(FeedComment feedComment, String email) {
        Member writer = feedComment.getMember();

        if (!writer.getEmail().equals(email)) {
            log.debug("FeedCommentServicef.checkEqualWriterAndUser exception occur " +
                    "feedComment : {},  email : {}", feedComment, email);
            throw new BusinessLogicException(ExceptionCode.COMMENT_WRITER_NOT_MATCH);
        }
    }

    private void checkDeletedComment(FeedComment feedComment) {
        if (feedComment.isDeleted()) {
            log.debug("FeedCommentService.checkDeletedComment exception occur " +
                    "feedComment : {}", feedComment);
            throw new BusinessLogicException(ExceptionCode.COMMENT_HAS_BEEN_DELETED);
        }
    }

    private FeedComment findFeedCommentById(Long commentId) {
        return feedCommentRepository.findById(commentId)
                .orElseThrow(() -> {
                    log.debug("FeedCommentService.findCommentById exception occur " +
                            "commentId : {}", commentId);
                    throw new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND);
                });
    }

    @Override
    protected <T> CommentDto.PostResponse createComment(CommentTypeDto<T> commentTypeDto) {
        return null;
    }

    @Override
    protected <T> CommentDto.PatchResponse updateComment(CommentTypeDto<T> commentTypeDto) {
        return null;
    }

    @Override
    protected void checkExistCommentById(Long id) {
        this.findFeedCommentById(id);
    }
}
