package MentosServer.mentos.model.dto;

import lombok.Data;

@Data
public class PostReportReq {
	
	private int mentoringId;
	
	private String report;
}
