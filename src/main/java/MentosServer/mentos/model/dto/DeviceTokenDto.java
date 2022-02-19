package MentosServer.mentos.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceTokenDto {
    private String currToken;
    private String newToken;

    //디바이스 생성 및 삭제
    public DeviceTokenDto(String currToken) {
        this.currToken = currToken;
    }

    //디바이스 변경
    public DeviceTokenDto(String currToken, String newToken) {
        this.currToken = currToken;
        this.newToken = newToken;
    }
}
