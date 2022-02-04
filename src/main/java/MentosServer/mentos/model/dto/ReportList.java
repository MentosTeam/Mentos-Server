package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class ReportList {
	
	private int idx;
	
	private String createAt;
	
	private String report;
}
