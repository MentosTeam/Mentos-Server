package MentosServer.mentos.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class PostProfileReq {
    @NonNull
    private int role;//멘토=1 or 멘티=2
    @NonNull
    private int memberId;
    @NonNull
    private int majorFirst;
    private int majorSecond;
    @NonNull
    private String introduction;
    private String imageUrl;
}
