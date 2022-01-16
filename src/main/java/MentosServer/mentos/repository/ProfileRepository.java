package MentosServer.mentos.repository;

import MentosServer.mentos.model.dto.PostProfileReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ProfileRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //멘토 프로필 등록
    public int createMentoProfile(PostProfileReq postProfileReq){
        String query = "insert into MENTO(memberId, mentoMajorFirst, mentoMajorSecond, mentoMajorThird, mentoIntro, mentoImageUrl) values(?,?,?,?,?,?)";
        Object[] params = new Object[]{postProfileReq.getMemberId(), postProfileReq.getMajorFirst(), postProfileReq.getMajorSecond(), postProfileReq.getMajorThird(), postProfileReq.getIntroduction(), postProfileReq.getImageUrl()};
        return this.jdbcTemplate.update(query, params);
    }

    //멘티 프로필 등록
    public int createMentiProfile(PostProfileReq postProfileReq){
        String query = "insert into MENTI(memberId, mentiMajorFirst, mentiMajorSecond, mentiMajorThird, mentiIntro, mentiImageUrl) values(?,?,?,?,?,?)";
        Object[] params = new Object[]{postProfileReq.getMemberId(), postProfileReq.getMajorFirst(), postProfileReq.getMajorSecond(), postProfileReq.getMajorThird(), postProfileReq.getIntroduction(), postProfileReq.getImageUrl()};
        return this.jdbcTemplate.update(query, params);
    }

    //멘토 프로필 중복 여부 확인
    public int checkMentoProfile(int memberId){
        String query = "select exists (select memberId from MENTO where memberId = ?)";
        int param = memberId;
        return this.jdbcTemplate.queryForObject(query, int.class, param);
    }

    //멘티 프로필 중복 여부 확인
    public int checkMentiProfile(int memberId){
        String query = "select exists (select memberId from MENTI where memberId = ?)";
        int param = memberId;
        return this.jdbcTemplate.queryForObject(query, int.class, param);
    }
}
