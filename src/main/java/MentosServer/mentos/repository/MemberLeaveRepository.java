package MentosServer.mentos.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class MemberLeaveRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //비밀번호 가져오기
    public String getPassword(int memberId){
        String query = "select memberPw from MEMBER where memberId=?";
        return this.jdbcTemplate.queryForObject(query, String.class, memberId);
    }

    //회원탈퇴 => 회원 상태 변경
    public int modifyMemberStatus(int memberId){
        String query = "update MEMBER set memberStatus = 'INACTIVE' where memberId=?";
        return this.jdbcTemplate.update(query, memberId);
    }
}
