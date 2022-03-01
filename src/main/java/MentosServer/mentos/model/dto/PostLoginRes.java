package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class PostLoginRes {
    private int memberId;
    private String jwt;
    private int mentor;
    private int mentee;
    private String memberNickName;
    private String mentorImage;
    private String menteeImage;
    private int genderFlag; //성별 공개 여부 1이면 on 0이면 off
    private int mentorNotificationFlag; //푸시알림 설정 1이면 on 0이면 off
    private int menteeNotificationFlag;
}
