package MentosServer.mentos.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Positive;

@Data
public class PostProfileImageReq {
    @Positive(message="POST_PROFILE_INVALID_ROLE")
    private int role;

    private String imageUrl; //null 가능
    private MultipartFile imageFile; //null 가능

}
