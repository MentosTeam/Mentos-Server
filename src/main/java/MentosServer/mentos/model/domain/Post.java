package MentosServer.mentos.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
public class Post {
    private int postId;
    private int majorCategoryId;
    private int memberId;
    private String postTitle;
    private String postContents;

    //게시글 등록의 경우
    public Post(int majorCategoryId, int memberId, String postTitle, String postContents) {
        this.majorCategoryId = majorCategoryId;
        this.memberId = memberId;
        this.postTitle = postTitle;
        this.postContents = postContents;
    }
}
