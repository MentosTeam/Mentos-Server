package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class MenteeWithNickName implements Comparable<MenteeWithNickName>{
	
	private int memberId;
	
	private int memberStudentId;
	
	private int mentiMajorFirst;
	
	private int mentiMajorSecond;
	
	private String memberNickName;
	
	private String mentiImage;
	
	private String mentiIntro;
	
	private Timestamp mentiCreateAt;
	
	private Timestamp mentiUpdateAt;
	
	/**
	 * 정렬을 위한 method
	 * @param mentee
	 * @return
	 */
	@Override
	public int compareTo(MenteeWithNickName mentee) {
		if(getMentiUpdateAt().toLocalDateTime().isAfter(mentee.getMentiUpdateAt().toLocalDateTime())) return 1;
		else return 0;
	}
}
