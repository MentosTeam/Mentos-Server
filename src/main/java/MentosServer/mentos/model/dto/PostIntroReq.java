package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostIntroReq {
    @Positive(message="POST_PROFILE_INVALID_ROLE")
    private int role;// 멘토 =1 or 멘티 =2

    @NotBlank(message="POST_PROFILE_EMPTY_INTRODUCTION")
    private String intro;
}
