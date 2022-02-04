package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostMentoringRes {
    private int mentoringId;
    private int mentoId; //멘토의 memberId
    private int mentiId; //멘티의 memberId
}
