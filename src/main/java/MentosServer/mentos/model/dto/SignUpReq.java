package MentosServer.mentos.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static MentosServer.mentos.config.BaseResponseStatus.EMPTY_USER_NAME;
import static MentosServer.mentos.config.Constant.nameRegex;
import static MentosServer.mentos.config.Constant.passwordRegex;

@Data

public class SignUpReq {

    @NotBlank(message="EMPTY_USER_NAME")
    @Pattern(regexp=nameRegex,message="EMPTY_USER_NAME")
    private String memberName;

    @NotBlank(message="EMPTY_USER_NICKNAME")
    @Pattern(regexp=nameRegex,message="EMPTY_USER_NICKNAME")
    private String memberNickName;//수정

    @NotBlank(message="EMPTY_USER_SEX")
    private String memberSex;

    @NotNull(message="EMPTY_USER_STUDENT_ID")
    private int memberStudentId; //수정

    @NotBlank(message="EMPTY_USER_SCHOOL_NAME")
    private String memberSchoolName;

    @NotBlank(message="POST_USERS_EMPTY_EMAIL")
    @Email(message="POST_USERS_INVALID_EMAIL")
    private String memberEmail;

    @NotBlank(message="EMPTY_USER_PASSWORD")
    @Pattern(regexp=passwordRegex,message="INVALID_USER_PASSWORD")
    private String memberPw;
}
