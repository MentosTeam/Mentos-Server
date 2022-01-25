package MentosServer.mentos.model.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class GetMenteeSearchReq {
	
	private ArrayList<String> majorFlag = new ArrayList<>();
	
	private String searchText = "";
	
}
