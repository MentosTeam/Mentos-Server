package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MainOtherMentorRes {
	
	private String nickName;
	
	private String mentorMajor; // 입력한 전공
	
	private String mentorImage;
	
	private String mentorYear;
	
	private int mentorStudentId; // 글 쓴 멘토 Id

	private int firstMajorCategory;
	
	private int secondMajorCategory;
}
