package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetMentorMainRes {

	private int mentos;
	
	private List<MenteeCategory> MenteeCategory;
	
	private List<MainMenteeDto> otherMentee;
	
}