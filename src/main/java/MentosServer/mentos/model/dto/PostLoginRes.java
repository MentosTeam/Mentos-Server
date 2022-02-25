package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class PostLoginRes {
    private int memberId;
    private String jwt;
    private int mentor;
    private int mentee;
    private String memberNickName;
    private String mentorImage;
    private String menteeImage;
}
