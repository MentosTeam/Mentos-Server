package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MainDto {
	
	private String schoolId;
	
	private int mentos;
	
	private int majorFirst;
	
	private int majorSecond;
}
