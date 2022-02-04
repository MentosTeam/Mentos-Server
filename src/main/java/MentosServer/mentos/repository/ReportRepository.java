package MentosServer.mentos.repository;

import MentosServer.mentos.model.dto.ReportList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ReportRepository {
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void postReport(int mentoringId, String report){
		String query = "INSERT INTO report VALUES (default, ?, ?, current_timestamp, current_timestamp)";
		Object params[] = new Object[]{report, mentoringId};
		this.jdbcTemplate.update(query, params);
	}
	
	/**
	 * Mentoring Count가 1이 아닐때만 감소
	 * 1이면 마지막 멘토링이므로 수행 안함 -> 0 반환
	 * @param mentoringId
	 * @return
	 */
	public int subMentoringCnt(int mentoringId) {
		String query = "UPDATE mentoring SET mentoringCount = mentoringCount - 1 WHERE mentoringCount != 1 AND mentoringId = ?";
		int param = mentoringId;
		return this.jdbcTemplate.update(query, param);
	}
	
	public void stopMentoring(int mentoringId) {
		String query = "UPDATE mentoring SET mentoringStatus = 2 WHERE mentoringId = ?";
		int param = mentoringId;
		this.jdbcTemplate.update(query, param);
	}
	
	public List<ReportList> getReport(int mentoringId) {
		int idx = 1;
		String query = "SELECT * FROM report WHERE mentoringId = ? ORDER BY reportCreateAt ASC";
		return this.jdbcTemplate.query(query,
				(rs, rowNum) -> new ReportList(
						rowNum + 1,
						rs.getString("reportCreateAt"),
						rs.getString("reportText")
				),
				mentoringId);
	}
}
