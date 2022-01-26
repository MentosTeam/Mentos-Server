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
    private int memberMajorId;
    private String memberSex;
    private String memberImage;
    private int memberMentos;
    private String memberStatus;
    private Timestamp memberCreateAt;
    private Timestamp memberUpdateAt;
}

