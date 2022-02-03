package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetMenteeProfileRes {
    private String memberNickName;
    private int memberStudentId;
    private String memberMajor;
    private int menteeMajorFirst;
    private int menteeMajorSecond;
    private String menteeImage;
    private String menteeIntro;
}
