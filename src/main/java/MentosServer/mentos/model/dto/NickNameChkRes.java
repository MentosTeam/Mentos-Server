package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NickNameChkRes {
    private int existNickName; //1이면 존재 0면 존재x


}
