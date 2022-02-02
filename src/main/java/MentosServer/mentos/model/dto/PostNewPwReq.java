package MentosServer.mentos.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static MentosServer.mentos.config.Constant.passwordRegex;

@Data
public class PostNewPwReq {
    @NotBlank(message="EMPTY_TMP_PASSWORD")
    private String tmpPw; //임시 비밀번호

    @NotBlank(message="EMPTY_USER_PASSWORD")
    @Pattern(regexp=passwordRegex,message="INVALID_USER_PASSWORD")
    private String newPw; //현재 비밀번호
}
