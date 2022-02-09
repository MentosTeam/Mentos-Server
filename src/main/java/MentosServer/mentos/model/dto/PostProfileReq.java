package MentosServer.mentos.model.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class PostProfileReq {
    @NonNull
    private int role;//멘토=1 or 멘티=2
    @NonNull
    private int majorFirst;
    private int majorSecond;
    @NonNull
    private String introduction;
    private MultipartFile imageFile;//이미지 파일
}
