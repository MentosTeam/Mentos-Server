package MentosServer.mentos.repository;

import MentosServer.mentos.model.dto.MainDto;
import MentosServer.mentos.model.dto.MainMenteeDto;
import MentosServer.mentos.model.dto.MainMentorDto;
import MentosServer.mentos.model.dto.MainOtherMentorRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class MainRepository {
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired //readme 참고
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	/**
	 * 차단(신고)한 글 ID 가져오기
	 * @param memberId
	 * @return
	 */
	public List<String> getBlockPosts(String memberId){
		String query = "select distinct(postId) from blockpost where memberId = ?";
		return this.jdbcTemplate.query(query,
				(rs, rowNum) -> Integer.toString(rs.getInt("postId")),
				memberId);
	}
	
	/**
	 * 차단(신고)한 멤버 ID 가져오기
	 * @param memberId
	 * @return
	 */
	public List<String> getBlockUsers(String memberId){
		String query = "select distinct(blockMemberId) from blockmember where memberId = ?";
		return this.jdbcTemplate.query(query,
				(rs, rowNum) -> Integer.toString(rs.getInt("blockMemberId")),
				memberId);
	}
	
	/**
	 * 멘토의 학교 ID, 선택한 전공들 반환
	 * @param memberId
	 * @return
	 */
	public MainDto getMentorData(String memberId){
		// Mento table에 접근해서 가져와야함
		String query = "select memberSchoolId, memberMentos, mentoMajorFirst, mentoMajorSecond from mento natural join member where memberId = ?";
		String param = memberId;
		return this.jdbcTemplate.queryForObject(query,
				(rs, rowNum) -> new MainDto(
						Integer.toString(rs.getInt("memberSchoolId")),
						rs.getInt("memberMentos"),
						rs.getInt("mentoMajorFirst"),
						rs.getInt("mentoMajorSecond")
				),
				param
		);
	}
	
	/**
	 * 멘토와 학교가 같은 멘티 중 선택한 전공이 같은 멘티들 최대 3명 반환
	 * 이때 update 최신 순으로 정렬
	 * 멘티의 1 전공 = 멘토의 1전공 or 멘티의 1전공 = 멘토의 2전공
	 * @param mainDto
	 * @return
	 */
	public List<MainMenteeDto> getMenteeList(MainDto mainDto, String memberId){ // union
		String blockUsers = String.join(",", getBlockUsers(memberId));
		if(blockUsers.equals("")) blockUsers = "0";
		
		String schoolId = mainDto.getSchoolId();
		String majorFirst = Integer.toString(mainDto.getMajorFirst());
		String majorSecond = Integer.toString(mainDto.getMajorSecond());
		
		String query = "(select * from menti natural join member where memberSchoolId = ? and mentiMajorFirst = ? and memberId NOT IN (" + blockUsers + ") order by mentiUpdateAt DESC LIMIT 3) " +
				"UNION " +
				"(select * from menti natural join member where memberSchoolId = ? and mentiMajorFirst = ? and memberId NOT IN (" + blockUsers + ") order by mentiUpdateAt DESC LIMIT 3) " +
				"UNION " +
				"(select * from menti natural join member where memberSchoolId = ? and mentiMajorFirst != ? and mentiMajorFirst != ? and memberId NOT IN (" + blockUsers + ") order by mentiUpdateAt DESC LIMIT 3)";
		Object[] searchParam = new Object[]{schoolId, majorFirst, schoolId, majorSecond, schoolId, majorFirst, majorSecond};
		
		return this.jdbcTemplate.query(query,
				(rs, rowNum) -> new MainMenteeDto(
						rs.getString("memberNickName"),
						rs.getString("memberMajor"),
						rs.getString("mentiImage"),
						Integer.toString(rs.getInt("memberStudentId")),
						rs.getInt("memberId"),
						rs.getInt("mentiMajorFirst"),
						rs.getInt("mentiMajorSecond")
				),
				searchParam
		);
	}
	
	/**
	 * 멘티의 학교 ID, 선택한 전공들 반환
	 * @param memberId
	 * @return
	 */
	public MainDto getMenteeData(String memberId){
		// Menti table에 접근해서 가져와야함
		String query = "select memberSchoolId, memberMentos, mentiMajorFirst, mentiMajorSecond from menti natural join member where memberId = ?";
		String param = memberId;
		return this.jdbcTemplate.queryForObject(query,
				(rs, rowNum) -> new MainDto(
						Integer.toString(rs.getInt("memberSchoolId")),
						rs.getInt("memberMentos"),
						rs.getInt("mentiMajorFirst"),
						rs.getInt("mentiMajorSecond")
				),
				param
		);
	}
	
	/**
	 * 멘티의 1전공, 2전공, 그외 전공에 관련된 글 최대 3개씩 찾아오기
	 * @param mainDto
	 * @return
	 */
	public List<MainMentorDto> getMentorList(MainDto mainDto, String memberId){ // union
		String blockPosts = String.join(",", getBlockPosts(memberId));
		if(blockPosts.equals("")) blockPosts = "0";
		
		String schoolId = mainDto.getSchoolId();
		String majorFirst = Integer.toString(mainDto.getMajorFirst());
		String majorSecond = Integer.toString(mainDto.getMajorSecond());
		String query = "(select * from (member natural join mento) natural join (post p left outer join image i on p.postId = i.postId) where memberSchoolId = ? and majorCategoryId = ? and p.postId NOT IN (" + blockPosts + ") order by mentoUpdateAt DESC LIMIT 3) " +
				"UNION " +
				"(select * from (member natural join mento) natural join (post p left outer join image i on p.postId = i.postId) where memberSchoolId = ? and majorCategoryId = ? and p.postId NOT IN (" + blockPosts + ") order by mentoUpdateAt DESC LIMIT 3) ";
		Object[] searchParam = new Object[]{schoolId, majorFirst, schoolId, majorSecond};
		return this.jdbcTemplate.query(query,
				(rs, rowNum) -> new MainMentorDto(
						rs.getString("memberNickName"),
						rs.getString("memberMajor"),
						rs.getString("mentoImage"),
						rs.getInt("memberStudentId"),
						rs.getInt("mentoMajorFirst"),
						rs.getInt("mentoMajorSecond"),
						rs.getInt("memberId"),
						rs.getInt("postId"),
						rs.getInt("majorCategoryId"),
						rs.getString("postTitle"),
						rs.getString("postContents"),
						rs.getString("imageUrl")
				),
				searchParam
		);
	}
	
	/**
	 * 멘티가 선택한 전공 외의 멘토들의 프로필만 제공하는 함수
	 * @param arr
	 * @return
	 */
	public List<MainOtherMentorRes> getOtherMentor(String memberId, int schoolId, ArrayList<String> arr){
		String blockUsers = String.join(",", getBlockUsers(memberId));
		if(blockUsers.equals("")) blockUsers = "0";
		
		String arrToStr = String.join(",", arr);
		String query = "select * from mento natural join member " +
				"where memberSchoolId = ? and mentoMajorFirst not in (" + arrToStr + ")  and mentoMajorSecond not in (" + arrToStr +") and memberId NOT IN (" + blockUsers + ") LIMIT 3";
		return this.jdbcTemplate.query(
				query,
				(rs, rowNum) -> new MainOtherMentorRes(
						rs.getString("memberNickName"),
						rs.getString("memberMajor"),
						rs.getString("mentoImage"),
						Integer.toString(rs.getInt("memberStudentId")) + "학번",
						rs.getInt("memberId"),
						rs.getInt("mentoMajorFirst"),
						rs.getInt("mentoMajorSecond")
				), schoolId
		);
	}
	
}
