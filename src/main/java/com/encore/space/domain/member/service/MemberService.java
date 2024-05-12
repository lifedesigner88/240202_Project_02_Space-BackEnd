package com.encore.space.domain.member.service;

import com.encore.space.common.config.PasswordConfig;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final EmailService emailService;
    private final ChangeType changeType;
    private final PasswordConfig passwordConfig;

    @Autowired
    public MemberService(
            MemberRepository memberRepository,
            EmailService emailService,
            ChangeType changeType, PasswordConfig passwordConfig
    ) {
        this.memberRepository = memberRepository;
        this.emailService = emailService;
        this.changeType = changeType;
        this.passwordConfig = passwordConfig;
    }

    public Member getMemberByAuthetication (){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return findByEmail(email);
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

//    세종 만듬
    public List<MemberResDto> findAllMembers() {
        return memberRepository.findAll()
                .stream()
                .filter(member -> member.getDelYn().equals("N"))
                .map(changeType::memberTOmemberResDto)
                .collect(Collectors.toList());
    }

    public MemberResDto getMyInfo() {
        return changeType.memberTOmemberResDto(getMemberByAuthetication());
    }

    public MemberResDto deleteMemberUseingDelYn() {
        Member member = getMemberByAuthetication();
        member.deleteMember();
        return changeType.memberTOmemberResDto(
                memberRepository.save(member));
    }

    public MemberResDto patchMemberInfo(MemberReqDto dto) {
        Member member = getMemberByAuthetication();
        member.updateMember(dto.getName(),dto.getNickname());
        return changeType.memberTOmemberResDto(
                memberRepository.save(member));
    }

    public MemberResDto updateProfile(MultipartFile profile) {
        Member member = getMemberByAuthetication();
        UUID uuid = UUID.randomUUID();
        String profileName = uuid + member.getEmail() + "_profile_" + profile.getOriginalFilename();
        Path profilePath = Paths.get(System.getProperty("user.dir") + "/src/main/java/com/encore/space/images/profiles", profileName);
        try {
            Files.write(profilePath, profile.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            member.setProfile(profilePath.toString());
        } catch (IOException e) {
            throw new RuntimeException("프로필 업로드에 실패했습니다.");
        }
        return changeType.memberTOmemberResDto(memberRepository.save(member));
    }

    public Resource getProfileImage(String email) {
        Member member = findByEmail(email);
        String profilePath = member.getProfile();
        try {
            Path path = Paths.get(profilePath);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("프로필 사진이 없습니다.");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("프로필을 가지고 오는중에 오류 발생");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public MemberResDto updatePassword(String password) {
        Member member = getMemberByAuthetication();
        String encodedPass = passwordConfig.passwordEncoder().encode(password);
        member.setPassword(encodedPass);
        return changeType.memberTOmemberResDto(memberRepository.save(member));
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
