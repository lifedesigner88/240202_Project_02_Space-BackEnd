package com.encore.space.common;


import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.member.domain.Role;
import com.encore.space.domain.member.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader implements CommandLineRunner {
    // CommandLineRunner를 통해 스프링빈으로 등록되는 시점에 run 메서드 실행
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public InitialDataLoader(
            MemberRepository memberRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if(memberRepository.findByEmail("encorespace3@gmail.com").isEmpty()){
            Member adminMember = Member.builder()
                    .name("SpaceManager")
                    .email("encorespace3@gmail.com")
                    .nickname("설현매니저")
                    .password(passwordEncoder.encode("88959697"))
                    .role(Role.MANAGER)
                    .delYn("N")
                    .build();
            memberRepository.save(adminMember);
        }
    }
}
