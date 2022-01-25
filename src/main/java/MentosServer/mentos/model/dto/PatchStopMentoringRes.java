package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatchStopMentoringRes {
    private int mentorindId;
    private int mentiId;
    private String status;
}
