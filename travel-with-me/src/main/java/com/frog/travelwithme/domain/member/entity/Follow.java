package com.frog.travelwithme.domain.member.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FOLLOWER_ID")
    private Member follower;

    @ManyToOne
    @JoinColumn(name = "FOLLOWING_ID")
    private Member following;

    @Builder
    public Follow(Member follower, Member following) {
        this.follower = follower;
        this.following = following;
    }
}
