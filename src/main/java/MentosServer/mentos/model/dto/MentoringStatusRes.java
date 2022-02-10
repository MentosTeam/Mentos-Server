package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class MentoringStatusRes {
    private int mentoringId;
    private int mentoringCount;
    private int majorCategoryId;
    private int mentoringMentos;
    private int mentoringStatus;
    private Timestamp mentoringCreateAt;
    private Timestamp mentoringUpdateAt;
    private String mentoringMentorName;
    private String mentoringMenteeName;
}
