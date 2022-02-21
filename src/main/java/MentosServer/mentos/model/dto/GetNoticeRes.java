package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetNoticeRes {
    private int noticeId;
    private String content;
    private Timestamp createAt;
}
