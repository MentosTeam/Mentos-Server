package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MainMentorDto {
	
	private String nickName;
	
	private String mentorMajor; // 입력한 전공
	
	private String mentorImage;
	
	private int mentorStudentId; // 글 쓴 멘토 Id
	
	private int postId; // 해당 글 포스트 id
	
	private int postCategoryId; // 글의 카테고리 ID
	
	private String postTitle;
	
	private String postContents;
	
	private String postImgUrl;
}
