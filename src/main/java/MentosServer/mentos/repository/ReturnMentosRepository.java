package MentosServer.mentos.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ReturnMentosRepository {
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public int returnMentos(String memberId){
		String query = "select memberMentos from member where memberId = ?";
		String param = memberId;
		return jdbcTemplate.queryForObject(query, Integer.class, param);
	}
}
