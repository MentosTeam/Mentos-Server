package MentosServer.mentos.model.dto;

import MentosServer.mentos.model.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class PostWithImageDto {
    @NotNull
    private int postId;
    @NotNull
    private int majorCategoryId;
    @NotNull
    private int memberId;
    @NotNull
    private String postTitle;
    @NotNull
    private String postContents;

    private String ImageUrl;
}
