package MentosServer.mentos.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class PostProfileReq {
    @NonNull
    private int role;//멘토=1 or 멘티=2
    @NonNull
    private int memberId;
    @NonNull
    private int majorFirst;
    private int majorSecond;
    private int majorThird;
    @NonNull
    private String introduction;
    private String imageUrl;

}
