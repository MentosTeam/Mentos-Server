package MentosServer.mentos.model.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class GetMentorSearchReq {

	private ArrayList<String> majorFlag = new ArrayList<>();
	
	private String searchText = "";
}
