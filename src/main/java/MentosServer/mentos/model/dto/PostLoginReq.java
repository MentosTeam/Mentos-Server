package MentosServer.mentos.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)


public class PostLoginReq {
    private String memberEmail;
    private String memberPw;
}