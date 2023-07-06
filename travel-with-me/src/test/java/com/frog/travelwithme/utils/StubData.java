package com.frog.travelwithme.utils;


import com.frog.travelwithme.domain.buddy.controller.dto.BuddyDto;
import com.frog.travelwithme.domain.buddy.entity.Matching;
import com.frog.travelwithme.domain.buddy.entity.Recruitment;
import com.frog.travelwithme.domain.buddy.entity.RecruitmentComment;
import com.frog.travelwithme.domain.buddy.service.dto.RecruitmentCommentCreateDto;
import com.frog.travelwithme.domain.buddy.service.dto.RecruitmentCommentUpdateDto;
import com.frog.travelwithme.domain.common.DeletionEntity;
import com.frog.travelwithme.domain.common.comment.dto.CommentDto;
import com.frog.travelwithme.domain.feed.controller.dto.FeedDto;
import com.frog.travelwithme.domain.feed.controller.dto.TagDto;
import com.frog.travelwithme.domain.feed.entity.Feed;
import com.frog.travelwithme.domain.feed.entity.FeedComment;
import com.frog.travelwithme.domain.feed.entity.Tag;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto.SignUp;
import com.frog.travelwithme.domain.member.entity.Follow;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.global.security.auth.controller.dto.AuthDto;
import com.frog.travelwithme.global.security.auth.controller.dto.AuthDto.LoginDto;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import com.frog.travelwithme.global.utils.TimeUtils;
import lombok.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.frog.travelwithme.global.enums.EnumCollection.*;

/**
 * StubData 설명: 테스트를 위한 Stub data 관리
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/03
 **/
public class StubData {
    public static class MockMember {
        static final Long id = 1L;
        @Getter
        static String email = "e_ma-il@gmail.com";
        static String password = "Password1234!";
        @Getter
        static String nickname = "nickname";
        @Getter
        static String image = "defaultImageUrl";
        static String address = "address";
        static String introduction = "introduction";
        static Nation nation = Nation.KO;
        static String role = "USER";
        static Gender enumGender = Gender.MALE;
        static Gender patchEnumGender = Gender.FEMALE;
        static List<String> interests = new ArrayList<>(List.of("하이킹", "전시회", "즉흥형"));
        static List<String> patchInterests = new ArrayList<>(List.of("서핑", "사진 촬영", "계획형"));
        static LocalDateTime createdAt = LocalDateTime.now();
        static LocalDateTime lastModifiedAt = LocalDateTime.now();
        @Getter
        static String emailKey = "email";
        @Getter
        static String codeKey = "code";
        @Getter
        static String codeValue = "123456";
        @Getter
        static String authCodePrefix = "AuthCode ";

        static String emailOther = "emailOther@gmail.com";
        static String nicknameOther = "nicknameOther";

        public static SignUp getSignUpDto() {
            return SignUp.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .address(address)
                    .nation(nation)
                    .gender(enumGender)
                    .role(role)
                    .interests(interests)
                    .build();
        }

        public static SignUp getFailedSignUpDtoByEmail(String failedEmail) {
            return SignUp.builder()
                    .email(failedEmail)
                    .password(password)
                    .nickname(nickname)
                    .address(address)
                    .gender(enumGender)
                    .nation(nation)
                    .role(role)
                    .build();
        }

        public static SignUp getSignUpDtoByEmailAndNickname(String email, String nickname) {
            return SignUp.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .address(address)
                    .gender(enumGender)
                    .nation(nation)
                    .role(role)
                    .build();
        }

        public static SignUp getFailedSignUpDtoByPassword(String failedPassword) {
            return SignUp.builder()
                    .email(email)
                    .password(failedPassword)
                    .nickname(nickname)
                    .address(address)
                    .gender(enumGender)
                    .nation(nation)
                    .role(role)
                    .build();
        }

        public static MockGenderFailSingUp getFailedSignUpDtoByGender(String failedGender) {
            return MockGenderFailSingUp.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .address(address)
                    .gender(failedGender)
                    .nation(nation)
                    .role(role)
                    .interests(interests)
                    .build();
        }

        public static LoginDto getLoginSuccessDto() {
            return LoginDto.builder()
                    .email(email)
                    .password(password)
                    .build();
        }

        public static LoginDto getLoginFailDto() {
            return LoginDto.builder()
                    .email("fail@gmail.com")
                    .password(password)
                    .build();
        }

        public static Member getMemberByEmailAndNickname(String email, String nickname) {
            return Member.builder()
//                    .id(id)
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .gender(enumGender)
                    .address(address)
                    .introduction(introduction)
                    .nation(nation)
                    .role(role)
                    .oauthstatus(OAuthStatus.NORMAL)
                    .build();
        }

        public static Member getMember() {
            return Member.builder()
                    .id(id)
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .gender(enumGender)
                    .address(address)
                    .introduction(introduction)
                    .nation(nation)
                    .role(role)
                    .oauthstatus(OAuthStatus.NORMAL)
                    .build();
        }

        public static MemberDto.Response getResponseDto() {
            return MemberDto.Response.builder()
                    .id(id)
                    .email(email)
                    .nickname(nickname)
                    .address(address)
                    .nation(nation)
                    .introduction(introduction)
                    .gender(enumGender)
                    .image(image)
                    .role(role)
                    .createdAt(createdAt)
                    .lastModifiedAt(lastModifiedAt)
                    .build();
        }

        public static MemberDto.Patch getPatchDto() {
            return MemberDto.Patch.builder()
                    .password("patch" + password)
                    .nickname("patch" + nickname)
                    .address("patch" + address)
                    .nation(Nation.JP)
                    .gender(patchEnumGender)
                    .introduction("patch" + introduction)
                    .interests(patchInterests)
                    .build();
        }

        public static AuthDto.LoginResponse getLoginResponseDto() {
            return AuthDto.LoginResponse.builder()
                    .id(id)
                    .email(email)
                    .nickname(nickname)
                    .role(role)
                    .build();
        }

        public static CustomUserDetails getUserDetails() {
            return CustomUserDetails.of(email, role);
        }

        public static CustomUserDetails getUserDetailsByEmailAndRole(String email, String role) {
            return CustomUserDetails.of(email, role);
        }

        public static BuddyDto.MatchingMemberResponse getMatchingRequestMemberResponse(Long id,
                                                                                             String nickname) {
            return BuddyDto.MatchingMemberResponse.builder()
                    .id(id)
                    .nickname(nickname)
                    .image(image)
                    .build();
        }

        public static Follow getFollow(Member follower, Member followee) {
            return Follow.builder()
                    .follower(follower)
                    .following(followee)
                    .build();
        }

        @Getter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        public static class MockGenderFailSingUp {
            private String email;
            private String password;
            private String nickname;
            private String gender;
            private Nation nation;
            private String address;
            private String role;
            private List<String> interests;
        }
    }

    public static class MockRecruitment {

        // 1번 Mock BuddyRecruitment 정보
        static Long id = 1L;
        static String title = "바하마 배편 동행 구해요";
        static String content = "1인 방예약이 너무비싸 쉐어하실분 구합니다!";
        static String travelNationality = "The Bahamas";
        static String travelStartDate = "2023-01-01";
        static String travelEndDate = "2023-01-03";
        static Long viewCount = 0L;
        static Long commentCount = 0L;

        // 2번 Mock BuddyRecruitment 정보
        static String patchTitle = "페루여행 쿠스코에서 콜릭티보 동행";
        static String patchContent = "콜렉티보 흥정이랑 같이 마추픽추까지 이동하실분 구해요!";
        static String patchTravelNationality = "Peru";
        static String patchTravelStartDate = "2023-01-30";
        static String patchTravelEndDate = "2023-01-31";


        public static Recruitment getRecruitment() {
            return Recruitment.builder()
                    .title(title)
                    .content(content)
                    .travelNationality(travelNationality)
                    .travelStartDate(TimeUtils.stringToLocalDateTime(travelStartDate))
                    .travelEndDate(TimeUtils.stringToLocalDateTime(travelEndDate))
                    .recruitmentStatus(RecruitmentStatus.IN_PROGRESS)
                    .deletionEntity(new DeletionEntity())
                    .build();
        }

        public static BuddyDto.RecruitmentPost getPostRecruitment() {
            return BuddyDto.RecruitmentPost.builder()
                    .title(title)
                    .content(content)
                    .travelNationality(travelNationality)
                    .travelStartDate(travelStartDate)
                    .travelEndDate(travelEndDate)
                    .build();
        }

        public static BuddyDto.RecruitmentPatch getPatchRecruitment() {
            return BuddyDto.RecruitmentPatch.builder()
                    .title(patchTitle)
                    .content(patchContent)
                    .travelNationality(patchTravelNationality)
                    .travelStartDate(patchTravelStartDate)
                    .travelEndDate(patchTravelEndDate)
                    .build();
        }

        public static BuddyDto.RecruitmentPostResponse getPostResponseRecruitment() {
            return BuddyDto.RecruitmentPostResponse.builder()
                    .title(title)
                    .content(content)
                    .travelNationality(travelNationality)
                    .travelStartDate(TimeUtils.stringToLocalDate(travelStartDate))
                    .travelEndDate(TimeUtils.stringToLocalDate(travelEndDate))
                    .viewCount(viewCount)
                    .commentCount(commentCount)
                    .nickname(MockMember.nickname)
                    .memberImage(MockMember.image)
                    .build();
        }

        public static BuddyDto.RecruitmentPatchResponse getPatchResponseRecruitment() {
            return BuddyDto.RecruitmentPatchResponse.builder()
                    .title(patchTitle)
                    .content(patchContent)
                    .travelNationality(patchTravelNationality)
                    .travelStartDate(TimeUtils.stringToLocalDate(patchTravelStartDate))
                    .travelEndDate(TimeUtils.stringToLocalDate(patchTravelEndDate))
                    .build();
        }

    }

    public static class MockMatching {
        public static Matching getMatching() {
            return Matching.builder()
                    .status(MatchingStatus.REQUEST)
                    .build();
        }
    }

    public static class MockFeed {
        static final String contents = "contents";
        static final String location = "location";
        static final String profileImage = "profileImage";
        static final String nickname = "nickname";
        static final boolean isLiked = false;
        @Getter
        static final String tagName = "tagName";
        static final Long commentCount = 100L;
        static final Long likeCount = 100L;
        static final Long tagCount = 100L;
        static final List<String> tags = List.of(tagName + "1", tagName + "2");
        @Getter
        static final int size = 20;
        static boolean isWriter = true;


        public static FeedDto.Post getPostDto() {
            return FeedDto.Post.builder()
                    .contents(contents)
                    .location(location)
                    .tags(tags)
                    .build();
        }

        public static FeedDto.Patch getPatchDto() {
            return FeedDto.Patch.builder()
                    .contents("patch" + contents)
                    .location("patch" + location)
                    .tags(List.of("patch" + tags.get(0)))
                    .removeImageUrls(List.of())
                    .build();
        }

        public static FeedDto.Response getResponseDto() {
            return FeedDto.Response.builder()
                    .contents(contents)
                    .location(location)
                    .profileImage(profileImage)
                    .liked(isLiked)
                    .writer(isWriter)
                    .nickname(nickname)
                    .likeCount(likeCount)
                    .commentCount(commentCount)
                    .tags(tags)
                    .build();
        }

        public static List<FeedDto.Response> getResponseDtos() {
            FeedDto.Response responseDto = getResponseDto();

            return List.of(responseDto);
        }

        public static FeedDto.ResponseDetail getResponseDetailDto() {
            return FeedDto.ResponseDetail.builder()
                    .contents(contents)
                    .prfileImage(profileImage)
                    .nickName(nickname)
                    // TODO: comments, tags 객체로 반환
                    .comments(List.of("comment"))
                    .tags(tags)
                    .build();
        }

        public static List<TagDto.Response> getTagResponseDtoList(int dtoCount) {
            List<TagDto.Response> responseList = new ArrayList<>();
            for (int i = 1; i <= dtoCount; i++) {
                responseList.add(getTagResponseDto(i));
            }

            return responseList;
        }

        public static Feed getFeed(Member member, Set<Tag> tags) {
            Feed feed = Feed.builder()
                    .contents(contents)
                    .location(location)
                    .imageUrls(List.of(profileImage))
                    .member(member)
                    .build();
            feed.addTags(tags);

            return feed;
        }

        private static TagDto.Response getTagResponseDto(int addName) {
            return TagDto.Response.builder()
                    .name(tagName + addName)
                    .count(tagCount)
                    .build();
        }
    }

    public static class CustomMockMultipartFile {

        public static MockMultipartFile getFile() {
            return new MockMultipartFile("file", "originalFilename.png",
                    MediaType.IMAGE_JPEG_VALUE, "fileContent".getBytes());
        }

        public static List<MockMultipartFile> getFiles() {
            MockMultipartFile file = new MockMultipartFile("files", "originalFilename.png",
                    MediaType.IMAGE_JPEG_VALUE, "fileContent".getBytes());
            return new ArrayList<>(List.of(file, file));
        }

        public static MockMultipartFile getData(String json) {
            return new MockMultipartFile("data", null,
                    MediaType.APPLICATION_JSON_VALUE, json.getBytes());
        }

        public static MockMultipartFile getFailFile() {
            return new MockMultipartFile("file", "originalFilename.gif",
                    MediaType.IMAGE_GIF_VALUE, "fileContent".getBytes());
        }
    }

    public static class CustomMultipartFile {

        @Getter
        static final String IMAGE_URL =
                "https://s3.ap-northeast-2.amazonaws.com/travel-with-me-fileupload/image/example.png";

        @Getter
        static final String FILE = "file";
        @Getter
        static final String FILES = "files";

        public static MultipartFile getMultipartFile() {
            return getFile(FILE);
        }

        public static List<MultipartFile> getMultipartFiles() {
            return new ArrayList<>(List.of(getFile(FILES), getFile(FILES)));
        }

        private static MultipartFile getFile(String multipartFileName) {
            return new MultipartFile() {
                @Override
                public String getName() {
                    return multipartFileName;
                }

                @Override
                public String getOriginalFilename() {
                    return "filename.png";
                }

                @Override
                public String getContentType() {
                    return MediaType.IMAGE_PNG_VALUE;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public long getSize() {
                    return "File content".getBytes().length;
                }

                @Override
                public byte[] getBytes() {
                    return "File content".getBytes();
                }

                @Override
                public InputStream getInputStream() {
                    return new ByteArrayInputStream("File content".getBytes());
                }

                @Override
                public void transferTo(File dest) throws IllegalStateException {

                }
            };
        }
    }

    public static class MockComment {
        static final Long commentId = 1L;
        static final Integer depth = 1;
        static final Long groupId = 1L;
        static final Long taggedMemberId = 1L;
        static final String content = "답글 입니다.";

        public static RecruitmentComment getRecruitmentComment() {
            return RecruitmentComment.builder()
                    .depth(depth)
                    .groupId(groupId)
                    .content(content)
                    .taggedMemberId(taggedMemberId)
                    .build();
        }

        public static FeedComment getFeedComment(Member member, Feed feed) {
            return FeedComment.builder()
                    .depth(depth)
                    .groupId(groupId)
                    .content(content)
                    .taggedMemberId(taggedMemberId)
                    .feed(feed)
                    .member(member)
                    .build();
        }

        public static RecruitmentComment getRecruitmentCommentByNoTaggedMemberId() {
            return RecruitmentComment.builder()
                    .depth(depth)
                    .groupId(groupId)
                    .content(content)
                    .build();
        }

        public static RecruitmentCommentCreateDto getRecruitmentCommentCreateDto() {
            return RecruitmentCommentCreateDto.builder()
                    .taggedMemberId(taggedMemberId)
                    .content(content)
                    .depth(depth)
                    .build();
        }

        public static RecruitmentCommentUpdateDto getRecruitmentCommentUpdateDto() {
            return RecruitmentCommentUpdateDto.builder()
                    .content(content)
                    .taggedMemberId(taggedMemberId)
                    .build();
        }

        public static CommentDto.Post getPostDtoByDepthAndGroupIdAndTaggedMemberId(Integer depth,
                                                                                   Long groupId,
                                                                                   Long taggedMemberId) {
            return CommentDto.Post.builder()
                    .depth(depth)
                    .groupId(groupId)
                    .content(content)
                    .taggedMemberId(taggedMemberId)
                    .build();
        }

        public static CommentDto.PostResponse getPostResponseDto() {
            return CommentDto.PostResponse.builder()
                    .commentId(commentId)
                    .depth(depth)
                    .groupId(groupId)
                    .content(content)
                    .taggedMemberId(taggedMemberId)
                    .build();
        }

        public static CommentDto.Patch getPatchDtoByContentAndTaggedMemberId(String content, Long taggedMemberId) {
            return CommentDto.Patch.builder()
                    .content(content)
                    .taggedMemberId(taggedMemberId)
                    .build();
        }

        public static CommentDto.PatchResponse getPatchResponseDto() {
            return CommentDto.PatchResponse.builder()
                    .commentId(commentId)
                    .depth(depth)
                    .content(content)
                    .taggedMemberId(taggedMemberId)
                    .build();
        }
    }
}
