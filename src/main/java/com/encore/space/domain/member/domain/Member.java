package com.encore.space.domain.member.domain;

import com.encore.space.common.domain.BaseEntity;
import com.encore.space.domain.chat.domain.MemberChatRoom;
import com.encore.space.domain.post.domain.Post;
import com.encore.space.domain.space.domain.SpaceMember;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;

    @Enumerated(value = EnumType.STRING)
    private LoginType loginType;

    private String profile;

    @Builder.Default
    private String delYn = "N";

    @Builder.Default
    @OneToMany(fetch = LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberChatRoom> memberChatRooms = new ArrayList<>();

    // 회원 삭제를 위한 연결
    @Builder.Default
    @OneToMany(fetch = LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private List<SpaceMember> spaceMembers = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();


    public void deleteMember(){
        this.delYn = "Y";
        this.email = id + "*_Deleted_*" + this.email; // 같은이메일 재 가입시.
        for (SpaceMember spaceMember : spaceMembers)
            if (spaceMember.getMember().getId().equals(this.id))
                spaceMember.setDelYn("Y");
        for (Post post : posts)
            if (post.getMember().getId().equals(this.id))
                post.deletePost();
    }


}
