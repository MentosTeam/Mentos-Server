package MentosServer.mentos.repository;

import MentosServer.mentos.model.domain.Member;
import MentosServer.mentos.model.dto.PostPasswordReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class PasswordRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public int checkEmail(PostPasswordReq req) {
        String checkEmailQuery = "select exists(select memberId from member where memberEmail =?)";
        return this.jdbcTemplate.queryForObject(checkEmailQuery,int.class,req.getMemberEmail()); //존재하면 1 아니면 0
    }

    // 비밀번호 설정
    public void setPw(String AESPw,int memberId){
        String setPwQuery = "update member set memberPw =?, memberUpdateAt = CURRENT_TIMESTAMP() where memberId=?";
        Object params[] = new Object[]{AESPw, memberId};
        this.jdbcTemplate.update(setPwQuery,params);
    }
    //이메일로 멤버인덱스 가져오는 함수
    public int getMemberId(String email){
        String getMemberIdQuery = "select memberId from member where memberEmail =?";
        return this.jdbcTemplate.queryForObject(getMemberIdQuery,int.class,email);
    }

    public Member getMember(int memberId) {
        String getPwdQuery = "select * from member where memberId = ?"; // 해당 email을 만족하는 User의 정보들을 조회한다.
        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs, rowNum) -> new Member(
                        rs.getInt("memberId"),
                        rs.getString("memberName"),
                        rs.getString("memberNickName"),
                        rs.getInt("memberStudentId"),
                        rs.getString("memberEmail"),
                        rs.getString("memberPw"),
                        rs.getInt("memberSchoolId"),
                        rs.getString("memberMajor"),
                        rs.getString("memberSex"),
                        rs.getInt("memberMentos"),
                        rs.getString("memberStatus"),
                        rs.getTimestamp("memberCreateAt"),
                        rs.getTimestamp("memberUpdateAt")
                ),
                memberId
        );
    }

}
