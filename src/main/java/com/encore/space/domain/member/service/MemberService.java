package com.encore.space.domain.member.service;

import com.encore.space.common.domain.ChangeType;
import com.encore.space.domain.email.dto.EmailCodeReqDto;
import com.encore.space.domain.email.dto.EmailReqDto;
import com.encore.space.domain.email.service.EmailService;
import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.member.dto.reqdto.MemberReqDto;
import com.encore.space.domain.member.dto.resdto.MemberResDto;
import com.encore.space.domain.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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

    public Member findByNickname (String nickname) throws EntityNotFoundException {
        return memberRepository.findByNickname(nickname).orElseThrow(()->new EntityNotFoundException("존재하지 않는 닉네임 입니다."));
    }

    public Member findByEmail (String email) throws IllegalArgumentException {
        return memberRepository.findByEmail(email).orElseThrow(()->new IllegalArgumentException("존재하지 않는 이메일 입니다."));
    }

    public boolean existsByEmail(String email){
        return memberRepository.existsByEmail(email);
    }

    public MemberResDto memberCreate(MemberReqDto memberReqDto) throws DataIntegrityViolationException {
        if(this.existsByEmail(memberReqDto.getEmail())){
            throw new DataIntegrityViolationException("이미 가입된 이메일입니다. 다른 이메일을 이용하세요.");
        }

        return changeType.memberTOmemberResDto(
                memberRepository.save(
                        changeType.memberReqDtoTOmember(memberReqDto)
                )
        );
    }

    public String emailAuthentication(EmailReqDto emailReqDto) throws MessagingException, NoSuchAlgorithmException, UnsupportedEncodingException {
        if(!emailService.isValidEmail(emailReqDto.getEmail())){
            throw new IllegalArgumentException("이메일 형식을 확인해 주세요.");
        }
        if(this.existsByEmail(emailReqDto.getEmail())){
            throw new DataIntegrityViolationException("이미 가입된 이메일입니다. 다른 이메일을 이용하세요.");
        }

        String number = emailService.makeRandomCode(emailReqDto.getEmail());
        emailService.SendEmail(
                emailReqDto.getEmail(),
                "엔코아 스페이스에서 이메일 인증입니다.",
                "인증번호는" + number + "입니다."
        );
        return number;
    }

    public void emailCheck(EmailCodeReqDto emailCodeReqDto) {
        if(!emailService.VerificationCode(emailCodeReqDto.getEmail(), emailCodeReqDto.getCode())){
            throw new IllegalArgumentException("인증번호가 다릅니다.");
        }
    }

//    public Optional<Member> getMemberWithAuthorities(@AuthenticationPrincipal CustomUserDetails userDetails) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null) {
//            log.info("Security Context 정보 없음");
//            return Optional.empty();
//        }
//
//    }
}
