package MentosServer.mentos.repository;

import MentosServer.mentos.model.dto.GetComplainReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ComplainRepository {
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void insertBlockMember(int memberId, int blockMemberId){
		String query = "INSERT INTO BLOCKMEMBER VALUES (?, ?)";
		this.jdbcTemplate.update(query, memberId, blockMemberId);
	}
	
	public void insertBlockPost(int memberId, int postId){
		String query = "INSERT INTO BLOCKPOST VALUES (?, ?)";
		this.jdbcTemplate.update(query, memberId, postId);
	}
	
	public void saveComplain(GetComplainReq req, int memberId){
		if(req.getFlag() == 1){
			insertBlockMember(memberId, req.getNumber());
		}
		else {
			insertBlockPost(memberId, req.getNumber());
		}
	}
}
