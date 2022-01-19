package MentosServer.mentos.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)


public class PostLoginReq {
    private String memberEmail;
    private String memberPw;
}