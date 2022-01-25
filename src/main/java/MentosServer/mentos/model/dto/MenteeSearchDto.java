package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenteeSearchDto {
	
	private String mentiId;
	
	private String mentiNickName;
	
	private String mentiMajorFirst;
	
	private String mentiMajorSecond;
	
	private String mentiImage;
	
	private String mentiIntro;
}
