package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
@AllArgsConstructor
public class PostPostsReq {
    @Positive(message = "EMPTY_MAJOR_CATEGORY_ID")
    private int majorCategoryId; //전공 카테고리 id
    @NotBlank(message="EMPTY_POST_TITLE")
    private String postTitle; //글 제목
    @NotEmpty(message="EMPTY_POST_CONTENTS")
    private String postContents; //글 내용
    private MultipartFile imageFile;//이미지 파일

}
