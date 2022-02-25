package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostProfileRes {
    private int memberId;
    private String profile;
    private String nickname;
    private String profileImgUrl;
}
