package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MainMentorDto {
	
	private String nickName;
	
	private String mentorMajor; // 입력한 전공
	
	private String mentorImage;
	
	private String menteeStudentId; // 학번
	
	private String firstMajorCategory;
	
	private String secondMajorCategory;
	
	private int majorCategoryId; // 글의 카테고리 ID
	
	private String postTitle;
	
	private String postContents;
	
	private String postImgUrl;
}
