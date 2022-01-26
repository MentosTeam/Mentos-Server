package MentosServer.mentos.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static MentosServer.mentos.config.Constant.nameRegex;

@Data
public class PostPasswordReq {
    @NotBlank(message="EMPTY_USER_NAME")
    @Pattern(regexp=nameRegex,message="EMPTY_USER_NAME")
    private String memberName; //이름

    @NotBlank(message="POST_USERS_EMPTY_EMAIL")
    @Email(message="POST_USERS_INVALID_EMAIL")
    private String memberEmail; //이메일
}
