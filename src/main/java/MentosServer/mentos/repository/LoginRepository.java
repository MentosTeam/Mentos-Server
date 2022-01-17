package MentosServer.mentos.repository;

import MentosServer.mentos.domain.model.Member;
import MentosServer.mentos.domain.model.PostLoginReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class LoginRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 로그인
    public Member getPwd(PostLoginReq postLoginReq) {
        String getPwdQuery = "select memberId, memberName, memberNickName, memberStudentId, memberEmail, memberPw, memberSchoolId, memberMajorId, memberSex, memberImage, MemberMentos, MemberStatus, memberCreateAt, memberUpdateAt from Member where memberEmail = ?"; // 해당 email을 만족하는 User의 정보들을 조회한다.
        String getPwdParams = postLoginReq.getMemberEmail(); // 주입될 email값을 클라이언트의 요청에서 주어진 정보를 통해 가져온다.

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs, rowNum) -> new Member(
                        rs.getInt("memberId"),
                        rs.getString("memberName"),
                        rs.getString("memberNickName"),
                        rs.getInt("memberStudentId"),
                        rs.getString("memberEmail"),
                        rs.getString("memberPw"),
                        rs.getInt("memberSchoolId"),
                        rs.getInt("memberMajorId"),
                        rs.getString("memberSex"),
                        rs.getString("memberImage"),
                        rs.getInt("memberMentos"),
                        rs.getString("memberStatus"),
                        rs.getTimestamp("memberCreateAt"),
                        rs.getTimestamp("memberUpdateAt")
                ),
                getPwdParams
        );
    }








}
