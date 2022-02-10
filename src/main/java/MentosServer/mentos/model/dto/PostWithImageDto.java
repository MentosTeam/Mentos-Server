package MentosServer.mentos.model.dto;

import MentosServer.mentos.model.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class PostWithImageDto {
    private int postId;
    private int majorCategoryId;
    private int mentoId;
    private String memberMajor;
    private String mentoNickName;
    private String postTitle;
    private String postContents;
    private String imageUrl;
}
