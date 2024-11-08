package com.intellipick.intern.presentaion.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestSignUpDto {

    @NotNull(message = "사용자 이름을 입력해주세요.")
    @NotBlank(message = "사용자 이름을 입력해주세요.")
    private String username;

    @NotNull(message = "비번을 입력해주세요.")
    @NotBlank(message = "비번을 입력해주세요.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
            message = "비밀번호는 대소문자, 숫자, 특수문자를 포함해야 합니다."
    )
    private String password;

    @NotNull(message = "닉네임을 입력해주세요.")
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;
}
