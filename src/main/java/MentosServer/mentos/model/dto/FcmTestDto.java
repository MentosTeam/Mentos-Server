package MentosServer.mentos.model.dto;

import MentosServer.mentos.utils.fcm.FcmMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class FcmTestDto {
    private String token;
    private Data data;

    @AllArgsConstructor
    @Getter
    public static class Data {
        private String title;
        private String body;
        private int receiverFlag;
    }
}
