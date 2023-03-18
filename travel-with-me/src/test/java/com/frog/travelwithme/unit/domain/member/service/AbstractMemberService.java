package com.frog.travelwithme.unit.domain.member.service;

import com.frog.travelwithme.domain.feed.service.FeedService;
import com.frog.travelwithme.domain.member.service.MemberService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractMemberService {

    @InjectMocks
    protected MemberService memberService;

    @Mock
    protected FeedService feedService;

}
