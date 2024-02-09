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
        if(memberRepository.findByEmail("encorespace@gmail.com").isEmpty()){
            Member member = Member.builder()
                    .name("SpaceManager")
                    .email("encorespace@gmail.com")
                    .nickname("설현매니저")
                    .password(passwordEncoder.encode("123456789"))
                    .role(Role.MANAGER)
                    .delYn("N")
                    .build();
            memberRepository.save(member);
        }
        if(memberRepository.findByEmail("ksg3941234@gmail.com").isEmpty()){
            Member member = Member.builder()
                    .name("SpaceTeacher")
                    .email("ksg3941234@gmail.com")
                    .nickname("김선국 강사님")
                    .password(passwordEncoder.encode("123456789"))
                    .role(Role.TEACHER)
                    .delYn("N")
                    .build();
            memberRepository.save(member);
        }
    }
}
