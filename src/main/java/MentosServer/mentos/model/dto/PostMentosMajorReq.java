package MentosServer.mentos.model.dto;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class PostMentosMajorReq {
    @Positive(message="POST_PROFILE_INVALID_ROLE")
    private int role; //1 - 멘토, 2 - 멘티

    private int mentosMajorFirst;
    private int mentosMajorSecond;

    public PostMentosMajorReq(int role, int mentosMajorFirst) {
        this.role = role;
        this.mentosMajorFirst = mentosMajorFirst;
    }

    public PostMentosMajorReq(int role, int mentosMajorFirst, int mentosMajorSecond) {
        this.role = role;
        this.mentosMajorFirst = mentosMajorFirst;
        this.mentosMajorSecond = mentosMajorSecond;
    }
}
