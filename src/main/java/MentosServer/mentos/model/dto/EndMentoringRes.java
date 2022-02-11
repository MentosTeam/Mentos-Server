package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EndMentoringRes {
    private int mentoringId;
    private int mentoringCount;
    private int majorCategoryId;
    private int mentoringMentos;
    private int reviewCheck;
    private String mentoringMentorName;
    private String mentoringMenteeName;

}
