package MentosServer.mentos.model.dto;

import lombok.Data;

@Data
public class GetSchoolCertificationReq {
	
	private String school;
	private String email;
}
