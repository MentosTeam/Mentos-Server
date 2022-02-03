package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetMentoProfileRes {
    private String memberNickName;
    private int memberStudentId;
    private String memberMajor;
    private int mentoMajorFirst;
    private int mentoMajorSecond;
    private String mentoImage;
    private String mentoIntro;

}
