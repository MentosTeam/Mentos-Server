package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
public class PostMentoringReq {
    @Positive(message = "POST_MENTORING_INVALID_MENTOID")
    private int mentoId; //멘토의 memberId

    private int mentiId = 0; //멘티의 memberId

    @Min(value = 1, message = "POST_MENTORING_LESS_MENTORINGCOUNT")
    @Max(value = 10, message = "POST_MENTORING_GERATER_MENTORINGCOUNT")
    private int mentoringCount;

    @Positive(message = "POST_MENTORING_INVALID_MAJORCATEGORYID")
    private int majorCategoryId;

    @PositiveOrZero(message = "POST_MENTORING_INVALID_MENTOS")
    private int mentos;
}
