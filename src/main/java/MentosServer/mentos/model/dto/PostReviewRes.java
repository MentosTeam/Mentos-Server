package MentosServer.mentos.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReviewRes {
    private int reviewId;
    private double reviewScore;


}