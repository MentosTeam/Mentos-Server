package MentosServer.mentos.repository;

import MentosServer.mentos.model.dto.GetComplainReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Slf4j
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
		log.info("flag : {}", req.getFlag());
		if(req.getFlag() == 2){
			log.info("insertBlockMember 실행");
			insertBlockMember(memberId, req.getNumber());
		}
		else {
			log.info("insertBlockPost 실행");
			insertBlockPost(memberId, req.getNumber());
		}
	}
}
