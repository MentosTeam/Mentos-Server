package MentosServer.mentos.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class GetNotificationRes {
    @JsonProperty("noticeId")
    private int notificationId;
    private String content;
    private Timestamp createAt;
}
