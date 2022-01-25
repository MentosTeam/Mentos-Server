package MentosServer.mentos.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class Mentoring {
    private int mentoringId;
    private int mentoringCount;
    private int majorCategoryId;
    private int mentoringMentos;
    private int mentoringMentoId;
    private int mentoringMentiId;
    private int mentoringStatus;
    private Timestamp mentoringCreateAt;
    private Timestamp mentoringUpdateAt;
}
