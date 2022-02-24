package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class GetNotificationRes {
    private int notificationId;
    private int memberId;
    private int statusFlag;
    private String content;
    private Timestamp updateAt;
}
