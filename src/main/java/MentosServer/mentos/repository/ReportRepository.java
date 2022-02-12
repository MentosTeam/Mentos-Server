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
	
	public int getMentoringCnt(int mentoringId) {
		String query = "SELECT Count(*) FROM Report WHERE mentoringId = ?";
		int param = mentoringId;
		return this.jdbcTemplate.queryForObject(query, Integer.class, param);
	}
	
	public void stopMentoring(int mentoringId) {
		String query = "UPDATE mentoring SET mentoringStatus = 2 WHERE mentoringId = ?";
		int param = mentoringId;
		this.jdbcTemplate.update(query, param);
	}
	
	public int getFinCnt(int mentoringId) {
		String query = "SELECT mentoringCount FROM mentoring WHERE mentoringId = ?";
		int param = mentoringId;
		return this.jdbcTemplate.queryForObject(query, Integer.class, param);
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
