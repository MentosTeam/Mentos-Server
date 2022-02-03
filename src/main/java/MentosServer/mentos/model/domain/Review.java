package MentosServer.mentos.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class Review {
    private int reviewId;
    private int mentoringId;
    private String reviewText;
    private double reviewScore;
    private Timestamp reviewCreateAt;
    private Timestamp reviewUpdateAt;
}
