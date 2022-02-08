package MentosServer.mentos.repository;

import MentosServer.mentos.model.dto.GetSchoolCertificationReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class SchoolCertificationRepository {
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired //readme 참고
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	/**
	 * 학교 이름으로 학교 email 찾아오기
	 * @param schoolName
	 * @return 학교 Email
	 */
	public String getSchoolEmail(String schoolName){
		String getEmailQuery = "select schoolemail from schoolcategory where schoolName = ?";
		String getEmailParam = schoolName;
		return this.jdbcTemplate.queryForObject(getEmailQuery, String.class, getEmailParam);
	}
	
	// 이메일 확인
	public int checkEmail(String email) {
		String checkEmailQuery = "select exists(select memberemail from member where memberemail = ?,memberStatus='active')"; // User Table에 해당 email 값을 갖는 유저 정보가 존재하는가?
		String checkEmailParams = email; // 해당(확인할) 이메일 값
		return this.jdbcTemplate.queryForObject(checkEmailQuery, int.class, checkEmailParams); // checkEmailQuery, checkEmailParams를 통해 가져온 값(intgud)을 반환한다. -> 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
	}
}
