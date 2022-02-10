package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenteeSearchDto {
	
	private int menteeStudentId;
	
	private String nickName;
	
	private String menteeYear;
	
	private int firstMajorCategory;
	
	private int secondMajorCategory;
	
	private String menteeImage;
	
	private String menteeMajor;
}
