package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
public class PostMentoringReq {
    @NotNull(message = "POST_MENTORING_EMPTY_MENTOID")
    @Min(value = 1, message = "POST_MENTORING_INVALID_MENTOID")
    private int mentoId; //멘토의 memberId

    @NotNull(message = "POST_MENTORING_EMPTY_MENTIID")
    private int mentiId = 0; //멘티의 memberId

    @NotNull(message = "POST_MENTORING_EMPTY_MENTORINGCOUNT")
    @Min(value = 1, message = "POST_MENTORING_LESS_MENTORINGCOUNT")
    @Max(value = 10, message = "POST_MENTORING_GERATER_MENTORINGCOUNT")
    private int mentoringCount;

    @NotNull(message = "POST_MENTORING_EMPTY_MAJORCATEGORYID")
    @Min(value = 1, message = "POST_MENTORING_INVALID_MAJORCATEGORYID")
    private int majorCategoryId;

    @NotNull(message = "POST_MENTORING_EMPTY_MENTOS")
    private int mentos;

}
