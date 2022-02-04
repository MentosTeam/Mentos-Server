package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MainMenteeDto {
	
	private String nickName;
	
	private String menteeMajor; // 입력한 전공
	
	private String menteeImage;
	
	private String menteeYear; // 학번
	
	private int menteeStudentId; // 멘티 Id
	
	private int firstMajorCategory;
	
	private int secondMajorCategory;
}
