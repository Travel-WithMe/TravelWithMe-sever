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
        super.checkPossibleToMakeGroup(postDto);
        Member member = memberService.findMember(email);
        Feed feed = feedService.findFeed(feedId);
        FeedComment feedComment = feedCommentRepository.save(
                feedCommentMapper.toEntity(postDto, member, feed));
        this.joinGroup(feedComment);

        return feedCommentMapper.toPostResponseDto(feedComment);
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

    }
}
