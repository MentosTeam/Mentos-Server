package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostAcceptMentoringRes {
    private int mentoringId;
    private int mentoId;
    private String status;
}
