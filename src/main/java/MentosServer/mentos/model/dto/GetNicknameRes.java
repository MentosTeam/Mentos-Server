package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetNicknameRes {
    private String mentoNickname;
    private String mentiNickname;
}
