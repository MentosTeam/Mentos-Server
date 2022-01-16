package MentosServer.mentos.model.dto;

import lombok.Data;

@Data
public class SignUpReq {
    private String memberName;
    private String memberNickName; //수정
    private String memberSex;
    private int memberStudentId; //수정
    private String memberSchoolName;
    private String memberEmail;
    private String memberPw;
}
