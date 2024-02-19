package com.encore.space.domain.member.domain;

import static jakarta.persistence.FetchType.LAZY;

import com.encore.space.common.domain.BaseEntity;
import com.encore.space.domain.chat.domain.MemberChatRoom;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


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

}
