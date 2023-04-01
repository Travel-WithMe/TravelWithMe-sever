package com.frog.travelwithme.domain.member.controller;

import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.entity.MemberDto;
import com.frog.travelwithme.domain.member.mapper.MemberMapper;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.global.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/29
 **/
@Slf4j
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper mapper;

    @PostMapping("/signup")
    public ResponseEntity postMember(@Valid @RequestBody MemberDto.SignUp signUpDto) {
        Member member = mapper.toEntity(signUpDto);
        Member saveMember = memberService.signUp(member);
        MemberDto.SingUpResponse response = mapper.toDto(saveMember);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.CREATED);
    }

    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") @Positive Long memberId) {
        Member findMember = memberService.findVerifiedMember(memberId);
        // TODO: Mapping 필요

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{member-id}")
    public ResponseEntity patchMember() {
        // TODO: Mapping, CustomBeanUtils 필요

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/reissue")
    public ResponseEntity reissue(HttpServletRequest request,
                                  HttpServletResponse response) {
        memberService.reissueAccessToken(request, response);

        return new ResponseEntity<>(new SingleResponseDto<>("The access token was successfully reissued"), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        memberService.logout(request);

        return new ResponseEntity<>(new SingleResponseDto<>("Logged out successfully"), HttpStatus.NO_CONTENT);
    }
}
