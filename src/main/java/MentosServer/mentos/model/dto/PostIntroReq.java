package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class PostIntroReq {
    @Positive(message="POST_PROFILE_INVALID_ROLE")
    private int role;// 멘토 =1 or 멘티 =2

    @Positive(message="POST_PROFILE_INVALID_MAJORFIRST")
    private int mentosMajorFirst;
    private int mentosMajorSecond;

    @NotBlank(message="POST_PROFILE_EMPTY_INTRODUCTION")
    private String intro;

    public PostIntroReq(int role,int mentosMajorFirst,String intro) {
        this.role = role;
        this.mentosMajorFirst = mentosMajorFirst;
        this.mentosMajorSecond=0;
        this.intro=intro;
    }

    public PostIntroReq(int role, int mentosMajorFirst, int mentosMajorSecond,String intro) {
        this.role = role;
        this.mentosMajorFirst = mentosMajorFirst;
        this.mentosMajorSecond = mentosMajorSecond;
        this.intro=intro;
    }

}
