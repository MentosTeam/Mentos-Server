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
    public int createMentoProfile(PostProfileReq postProfileReq, String imageUrl, int memberId){
        String query;
        Object[] params;

        if(imageUrl != null){
            query = "insert into MENTO(memberId, mentoMajorFirst, mentoMajorSecond, mentoIntro, mentoImage) values(?,?,?,?,?)";
            params = new Object[]{memberId, postProfileReq.getMajorFirst(), postProfileReq.getMajorSecond(), postProfileReq.getIntroduction(), imageUrl};
        }
        else{
            query = "insert into MENTO(memberId, mentoMajorFirst, mentoMajorSecond, mentoIntro) values(?,?,?,?)";
            params = new Object[]{memberId, postProfileReq.getMajorFirst(), postProfileReq.getMajorSecond(), postProfileReq.getIntroduction()};
        }
        return this.jdbcTemplate.update(query, params);
    }

    //멘티 프로필 등록
    public int createMentiProfile(PostProfileReq postProfileReq, String imageUrl, int memberId){
        String query;
        Object[] params;

        if(imageUrl != null){
            query = "insert into MENTI(memberId, mentiMajorFirst, mentiMajorSecond, mentiIntro, mentiImage) values(?,?,?,?,?)";
            params = new Object[]{memberId, postProfileReq.getMajorFirst(), postProfileReq.getMajorSecond(), postProfileReq.getIntroduction(), imageUrl};
        }
        else{
            query = "insert into MENTI(memberId, mentiMajorFirst, mentiMajorSecond, mentiIntro) values(?,?,?,?)";
            params = new Object[]{memberId, postProfileReq.getMajorFirst(), postProfileReq.getMajorSecond(), postProfileReq.getIntroduction()};
        }
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
