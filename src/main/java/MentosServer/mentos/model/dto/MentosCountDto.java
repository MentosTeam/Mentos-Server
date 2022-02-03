package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MentosCountDto {
    private int mentoringId;
    private int majorCategoryId;
    private int mentoringMentos;
}
