package com.frog.travelwithme.unit.domain.feed.service;

import com.frog.travelwithme.domain.feed.mapper.FeedMapper;
import com.frog.travelwithme.domain.feed.repository.FeedRepository;
import com.frog.travelwithme.domain.feed.service.FeedService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FeedServiceTest {

    @InjectMocks
    private FeedService feedService;

    @Mock
    private FeedRepository feedRepository;

    @Mock
    private FeedMapper feedMapper;

    @Test
    void FeedServiceTest1() throws Exception {
        // given


        // when


        // then

    }

    @Test
    void FeedServiceTest2() throws Exception {
        // given

        // when

        // then

    }

    @Test
    void FeedServiceTest3() throws Exception {
        // given

        // when

        // then

    }

    @Test
    void FeedServiceTest4() throws Exception {
        // given

        // when

        // then

    }

    @Test
    void FeedServiceTest5() throws Exception {
        // given

        // when

        // then

    }
}
