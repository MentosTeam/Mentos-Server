package MentosServer.mentos.model.dto;

import MentosServer.mentos.model.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetMentorProfileRes {
    private MentorProfileDto basicInformation;
    private String schoolName;
    private int numOfMentoring;
    private List<PostWithImageDto> postArr;
    private List<Review> reviews;
    private List<MentosCountDto> numOfMentos;
}
