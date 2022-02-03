package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetSettingProfileRes {
    private String memberNickName;
    private int memberStudentId;
    private String memberMajor;
    private int memberMajorFirst;
    private int memberMajorSecond;
    private String memberImage;
    private String memberIntro;

}
