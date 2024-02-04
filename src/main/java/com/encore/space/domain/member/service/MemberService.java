package com.encore.space.domain.member.service;

import com.encore.space.common.ChangeType;
import com.encore.space.common.service.EmailService;
import com.encore.space.domain.member.dto.reqdto.MemberReqDto;
import com.encore.space.domain.member.dto.resdto.MemberResDto;
import com.encore.space.domain.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Slf4j
@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final EmailService emailService;
    private final ChangeType changeType;

    @Autowired
    public MemberService(
            MemberRepository memberRepository,
            EmailService emailService,
            ChangeType changeType
    ) {
        this.memberRepository = memberRepository;
        this.emailService = emailService;
        this.changeType = changeType;
    }

    public MemberResDto memberCreate(MemberReqDto memberReqDto){
        return changeType.memberTOmemberResDto(
                memberRepository.save(
                        changeType.memberReqDtoTOmember(
                                memberReqDto
                        )
                )
        );
    }

    public String emailAuthentication(String email) throws MessagingException, NoSuchAlgorithmException {
        String number = emailService.makeRandomNumber(email);

        emailService.SendEmail(
            email,
        "엔코아 스페이스에서 이메일 인증입니다.",
            "인증번호는" + number + "입니다."
        );

        return number;
    }
}
