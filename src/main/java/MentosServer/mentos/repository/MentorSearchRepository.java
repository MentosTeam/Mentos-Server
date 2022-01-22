package MentosServer.mentos.repository;

import MentosServer.mentos.model.domain.Post;
import MentosServer.mentos.model.dto.GetMentorSearchReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Slf4j
@Repository
public class MentorSearchRepository {
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired //readme 참고
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public String getMajorById(String memberId){
		// Menti table에 접근해서 가져와야함
		String getMajorQuery = "select mentiMajorFirst from menti where memberId = ?";
		String getMajorParam = memberId;
		return this.jdbcTemplate.queryForObject(getMajorQuery, String.class, getMajorParam);
	}
	
	public String getSchoolById(String memberId){
		// Menti table에 접근해서 가져와야함
		String getMajorQuery = "select memberSchoolId from member where memberId = ?";
		String getMajorParam = memberId;
		return Integer.toString(this.jdbcTemplate.queryForObject(getMajorQuery, Integer.class, getMajorParam));
	}
	
	/**
	 * Post 테이블과 Member 테이블을 Join
	 * majorCategoryId가 일치하는 Tuple만 뽑아오기
	 * 그 중 멤버 닉네임, 글 제목, 글 내용 중에 일치하는 Tuple들만 반환
	 * 이때 현재 상태가 ACTIVE가 아니라면 제외(탈퇴 회원)
	 * 또한 같은 학교여야만 함 (현재 DB는 모든 학교의 멘토 같이 관리)
	 * @param req
	 * @return
	 */
	public List<Post> getPosts(GetMentorSearchReq req, String schoolId){
		String arrayToString = String.join(",", req.getMajorFlag());
		String searchQuery =
				"select postId, majorCategoryId, memberId, postTitle, postContents, postCreateAt, postUpdateAt " +
				"from " +
					"(" +
					"select * " +
					"from member NATURAL JOIN post " +
					"where majorCategoryId IN (" + arrayToString + ")" + " AND memberStatus = 'ACTIVE' AND memberSchoolId = " + schoolId +
					") T " +
				"where memberNickName LIKE ? OR postTitle LIKE ? OR postContents LIKE ?";
		String param = changeParam(req.getSearchText());
		Object[] searchParam = new Object[]{param, param, param};
		return this.jdbcTemplate.query(searchQuery,
				(rs, rowNum) -> new Post(
						rs.getInt("postId"),
						rs.getInt("majorCategoryId"),
						rs.getInt("memberId"),
						rs.getString("postTitle"),
						rs.getString("postContents"),
						rs.getTimestamp("postCreateAt"),
						rs.getTimestamp("postUpdateAt")
				),
				searchParam
		);
	}
	
	public String changeParam(String text){
		return "%" + text + "%";
	}
}
