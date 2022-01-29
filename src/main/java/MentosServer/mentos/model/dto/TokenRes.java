package MentosServer.mentos.model.dto;

import lombok.Data;

@Data
public class TokenRes {
    private final String memberJwt;
    private final String refreshJwt;
}
