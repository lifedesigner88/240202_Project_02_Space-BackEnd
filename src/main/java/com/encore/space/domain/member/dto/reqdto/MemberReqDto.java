package com.encore.space.domain.member.dto.reqdto;

import com.encore.space.domain.member.domain.LoginType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "회원가입을 위한 DTO")
@AllArgsConstructor
public class MemberReqDto {
    @Schema(description = "이름")
    @NotBlank(message="이름을 입력해주세요.")
    private String name;

    @Schema(description = "이메일")
    @NotBlank(message="이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    private String email;

    @Schema(description = "비밀번호")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 15, message = "비밀번호는 최소 8자 이상, 15자 이하의 숫자를 입력하세요")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,15}$", message = "비밀번호는 알파벳 대소문자(a~z, A~Z), 숫자(0~9)가 혼합되어야 합니다.")
    private String password;

    @Schema(description = "닉네임")
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    @NotNull
    @Schema(description = "로그인 타입")
    private LoginType loginType;

}
