package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MentorProfileDto {
    private int memberId;
    private String name;
    private String nickname;
    private int studentId;
    private int schoolId;
    private String major;
    private String sex;
    private int majorFirst;
    private int majorSecond;
    private String profileImage;
    private String intro;
    private double mentoScore;
}
