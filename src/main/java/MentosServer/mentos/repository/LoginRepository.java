package MentosServer.mentos.repository;

import MentosServer.mentos.model.domain.Member;
import MentosServer.mentos.model.dto.PostLoginReq;
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

    // 이메일 확인
    public int checkEmail(String email) {
        String checkEmailQuery = "select exists(select memberEmail from member where memberEmail = ?)"; // User Table에 해당 email 값을 갖는 유저 정보가 존재하는가?
        String checkEmailParams = email; // 해당(확인할) 이메일 값
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams); // checkEmailQuery, checkEmailParams를 통해 가져온 값(intgud)을 반환한다. -> 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
    }

    // 로그인
    public Member getPwd(PostLoginReq postLoginReq) {
        String getPwdQuery = "select * from member where memberEmail = ?"; // 해당 email을 만족하는 User의 정보들을 조회한다.
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
                        rs.getString("memberMajor"),
                        rs.getString("memberSex"),
                        rs.getInt("memberMentos"),
                        rs.getString("memberStatus"),
                        rs.getTimestamp("memberCreateAt"),
                        rs.getTimestamp("memberUpdateAt")
                ),
                getPwdParams
        );
    }


    // 멘토 프로필 확인
    public int checkMentor(int memberId) {
        String checkMentorQuery = "select exists(select memberId from mento where memberId = ?)"; // User Table에 해당 멤버 값을 갖는 유저 정보가 존재하는가?
        int checkMentorParams = memberId; // 해당(확인할) 멤버 값
        return this.jdbcTemplate.queryForObject(checkMentorQuery,
                int.class,
                checkMentorParams); // checkEmailQuery, checkEmailParams를 통해 가져온 값(intgud)을 반환한다. -> 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
    }


    // 멘티 프로필 확인
    public int checkMentee(int memberId) {
        String checkMenteeQuery = "select exists(select memberId from menti where memberId = ?)"; // User Table에 해당 멤버 값을 갖는 유저 정보가 존재하는가?
        int checkMenteeParams = memberId; // 해당(확인할) 멤버 값
        return this.jdbcTemplate.queryForObject(checkMenteeQuery,
                int.class,
                checkMenteeParams); // checkEmailQuery, checkEmailParams를 통해 가져온 값(intgud)을 반환한다. -> 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
    }

    public String getMentorImage(int memberId){
        String MentorSQL ="select mentoImage from mento where memberId = ?";
        return this.jdbcTemplate.queryForObject(MentorSQL,String.class, memberId);

    }

    public String getMenteeImage (int memberId){
        String MenteeSQL ="select mentiImage from menti where memberId = ?";
        return this.jdbcTemplate.queryForObject(MenteeSQL,String.class, memberId);

    }


    public int[] checkMemberFlag(int memberId) {
        String query = "select memberSexFlag, memberNotificationFlag from member where memberId=?";
        return this.jdbcTemplate.queryForObject(query,
                (rs,rowNum) ->new int[]{
                        rs.getInt("memberSexFlag"),
                        rs.getInt("memberNotificationFlag")
                },
                memberId
         );
    }
}
