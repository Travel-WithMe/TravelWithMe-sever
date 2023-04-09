package com.frog.travelwithme.unit.domain.member.repository;

import com.frog.travelwithme.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public abstract class AbstractMemberRepository {

    @Autowired
    protected MemberRepository memberRepository;

}
