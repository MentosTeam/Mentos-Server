package MentosServer.mentos.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor

public class Member {
    private int memberId;
    private String memberName;
    private String memberNickName;
    private int memberStudentId;
    private String memberEmail;
    private String memberPw;
    private int memberSchoolId;
    private String memberMajor;
    private String memberSex;
    private int memberMentos;
    private String memberStatus;
    private Timestamp memberCreateAt;
    private Timestamp memberUpdateAt;
}

