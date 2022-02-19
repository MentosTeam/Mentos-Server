package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetComplainReq {
	
	int flag;
	
	int number;
	
	String text;
}
