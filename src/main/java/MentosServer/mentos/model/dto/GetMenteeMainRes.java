package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetMenteeMainRes {
	
	private int mentos;
	
	private List<MentorCategory> MentorCategory;
	
	private List<MainMentorDto> otherMentor;
}
