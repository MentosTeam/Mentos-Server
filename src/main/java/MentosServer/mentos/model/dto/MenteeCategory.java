package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MenteeCategory {
	
	private int menteeCategoryId;
	
	private List<MainMenteeDto> mentee;
}
