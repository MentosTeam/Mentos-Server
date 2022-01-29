package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpRes {
    private int memberId;
    private String memberJwt;
    private String refreshJwt;

}
