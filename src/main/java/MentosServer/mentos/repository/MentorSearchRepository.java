package MentosServer.mentos.repository;

import MentosServer.mentos.model.dto.PostWithProfile;
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
		String getSchoolQuery = "select memberSchoolId from member where memberId = ?";
		String getSchoolParam = memberId;
		return Integer.toString(this.jdbcTemplate.queryForObject(getSchoolQuery, Integer.class, getSchoolParam));
	}
	
	public List<String> getBlockPosts(String memberId){
		String query = "select distinct(postId) from blockpost where memberId = ?";
		return this.jdbcTemplate.query(query,
				(rs, rowNum) -> Integer.toString(rs.getInt("postId")),
				memberId);
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
	public List<PostWithProfile> getPosts(GetMentorSearchReq req, String memberId, String schoolId){
		String blockPosts = String.join(",", getBlockPosts(memberId));
		if(blockPosts.equals("")) blockPosts = "0";
		String arrayToString = String.join(",", req.getMajorFlag());
		String searchQuery =
				"select postId, majorCategoryId, memberId, memberNickName, memberMajor, mentoImage, postTitle, postContents, postCreateAt, postUpdateAt " +
				"from " +
					"(" +
					"select * " +
					"from member NATURAL JOIN post " +
					"where memberStatus = 'ACTIVE' AND memberSchoolId = " + schoolId +
					") T NATURAL JOIN mento " +
				"where (majorCategoryId IN (" + arrayToString + ")) AND " + "(memberNickName LIKE ? OR postTitle LIKE ? OR postContents LIKE ?) " +
						"AND postId NOT IN (" + blockPosts + ")";
		String param = changeParam(req.getSearchText());
		Object[] searchParam = new Object[]{param, param, param};
		return this.jdbcTemplate.query(searchQuery,
				(rs, rowNum) -> new PostWithProfile(
						rs.getInt("postId"),
						rs.getInt("majorCategoryId"),
						rs.getInt("memberId"),
						rs.getString("memberNickName"),
						rs.getString("memberMajor"),
						rs.getString("mentoImage"),
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
	
	public List<String> getImageUrl(int postId){
		String searchImageQuery = "select imageUrl from Image where postId = ?";
		String searchImageParam = Integer.toString(postId);
		return this.jdbcTemplate.query(searchImageQuery,
				(rs, rowNum) -> rs.getString("imageUrl"),
				searchImageParam);
	}
}
