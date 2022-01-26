package MentosServer.mentos.model.dto.mentoring;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReviewReq {
    private int mentoringId;
    @NotBlank
    private double reviewScore;
    @NotBlank
    private String reviewText;

}
