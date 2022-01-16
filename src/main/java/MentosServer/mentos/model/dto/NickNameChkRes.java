package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NickNameChkRes {
    private boolean existNickName; //true면 존재 false면 존재x


}
