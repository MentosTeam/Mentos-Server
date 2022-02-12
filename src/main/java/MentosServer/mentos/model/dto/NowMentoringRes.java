package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NowMentoringRes {
    private int mentoringId;
    private int mentoringCount;
    private int majorCategoryId;
    private int mentoringMentos;
    private String mentoringMentorName;
    private String mentoringMenteeName;
    private int currentCount;
}
