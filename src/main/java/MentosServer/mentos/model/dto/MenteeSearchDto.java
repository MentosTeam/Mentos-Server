package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenteeSearchDto {
	
	private int mentiId;
	
	private String mentiNickName;
	
	private int memberStudentId;
	
	private int mentiMajorFirst;
	
	private int mentiMajorSecond;
	
	private String mentiImage;
	
	private String mentiIntro;
}
