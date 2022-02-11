package MentosServer.mentos.repository;

import MentosServer.mentos.model.dto.MenteeWithNickName;
import MentosServer.mentos.model.dto.GetMenteeSearchReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Slf4j
@Repository
public class MenteeSearchRepository {
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired //readme 참고
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public String getMajorById(String memberId){
		// Mento table에 접근해서 가져와야함
		String getMajorQuery = "select mentoMajorFirst from mento where memberId = ?";
		String getMajorParam = memberId;
		return this.jdbcTemplate.queryForObject(getMajorQuery, String.class, getMajorParam);
	}
	
	public String getSchoolById(String memberId){
		String getSchoolQuery = "select memberSchoolId from member where memberId = ?";
		String getSchoolParam = memberId;
		return Integer.toString(this.jdbcTemplate.queryForObject(getSchoolQuery, Integer.class, getSchoolParam));
	}
	
	public List<MenteeWithNickName> getMentee(GetMenteeSearchReq req, String schoolId){
		String arrayToString = String.join(",", req.getMajorFlag());
		String searchQuery =
				"SELECT memberId, memberStudentId, mentiMajorFirst, mentiMajorSecond, memberNickName, mentiImage, memberMajor, mentiCreateAt, mentiUpdateAt" +
						" FROM menti NATURAL JOIN member" +
						" WHERE (mentiMajorFirst IN (" + arrayToString + ") OR mentiMajorSecond IN (" + arrayToString + ")) AND memberStatus = 'ACTIVE' AND memberSchoolId = "+schoolId +
						" AND (memberName LIKE ? OR memberNickName LIKE ? OR mentiIntro LIKE ?)";
		String param = changeParam(req.getSearchText());
		Object[] searchParam = new Object[]{param, param, param};
		return this.jdbcTemplate.query(searchQuery,
				(rs, rowNum) -> new MenteeWithNickName(
						rs.getInt("memberId"),
						rs.getInt("memberStudentId"),
						rs.getInt("mentiMajorFirst"),
						rs.getInt("mentiMajorSecond"),
						rs.getString("memberNickName"),
						rs.getString("mentiImage"),
						rs.getString("memberMajor"),
						rs.getTimestamp("mentiCreateAt"),
						rs.getTimestamp("mentiUpdateAt")
				),
				searchParam
		);
	}
	
	public String changeParam(String text){
		return "%" + text + "%";
	}
	
	public List<String> getImageUrl(String postId){
		String searchImageQuery = "select imageUrl from Image where postId = ?";
		String searchImageParam = postId;
		return this.jdbcTemplate.query(searchImageQuery,
				(rs, rowNum) -> rs.getString("imageUrl")
				, searchImageParam
		);
	}
}
