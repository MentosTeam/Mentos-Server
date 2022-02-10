package MentosServer.mentos.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetMentoringStatus {
    private List<MentoringStatusRes> nowMentoring;
    private List<MentoringStatusRes> endMentoring;

}
