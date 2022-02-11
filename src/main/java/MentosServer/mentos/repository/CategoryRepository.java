package MentosServer.mentos.repository;

import MentosServer.mentos.model.dto.GetCategoryRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class CategoryRepository {
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public GetCategoryRes getMentorCategory(int memberId){
		String query = "select mentoMajorFirst, mentoMajorSecond from mento where memberId = ?";
		return jdbcTemplate.queryForObject(query,
				(rs, rowNum) -> new GetCategoryRes(
						rs.getInt("mentoMajorFirst"),
						rs.getInt("mentoMajorSecond")
				), memberId);
	}
	
	public GetCategoryRes getMenteeCategory(int memberId){
		String query = "select mentiMajorFirst, mentiMajorSecond from menti where memberId = ?";
		return jdbcTemplate.queryForObject(query,
				(rs, rowNum) -> new GetCategoryRes(
						rs.getInt("mentiMajorFirst"),
						rs.getInt("mentiMajorSecond")
				), memberId);
	}
}
