package com.encore.space.domain.member.service;

import com.encore.space.common.ChangeType;
import com.encore.space.common.service.*;
import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.member.dto.reqdto.MemberReqDto;
import com.encore.space.domain.member.dto.resdto.MemberResDto;
import com.encore.space.domain.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

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

    public Member findById (Long id) throws EntityNotFoundException {
        return memberRepository.findById(id).orElseThrow(()->new EntityNotFoundException("존재하지 않는 아이디 입니다."));
    }

    public Member findByEmail (String email) throws IllegalArgumentException {
        return memberRepository.findByEmail(email).orElseThrow(()->new IllegalArgumentException("존재하지 않는 이메일 입니다."));
    }

    public MemberResDto memberCreate(MemberReqDto memberReqDto) throws DataIntegrityViolationException {
        if(memberRepository.findByEmail(memberReqDto.getEmail()).isPresent()){
            throw new DataIntegrityViolationException("중복 이메일 입니다.");
        }

        return changeType.memberTOmemberResDto(
                memberRepository.save(
                        changeType.memberReqDtoTOmember(
                                memberReqDto
                        )
                )
        );
    }

    public String emailAuthentication(String email) throws MessagingException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String number = emailService.makeRandomCode(email);
        log.info("email : "+ email);
        emailService.SendEmail(
                email,
        "엔코아 스페이스에서 이메일 인증입니다.",
            "인증번호는" + number + "입니다."
        );
        return number;
    }

    public void emailCheck(String email, String code) {
        if(!emailService.VerificationCode(email, code)){
            throw new IllegalArgumentException("인증번호가 다릅니다.");
        }
    }
}
