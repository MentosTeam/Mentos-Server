package MentosServer.mentos.repository;

import MentosServer.mentos.model.domain.Mentoring;
import MentosServer.mentos.model.dto.PostMentoringReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class MentoringRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //멘토링 등록
    public int createMentoring(PostMentoringReq postMentoringReq){
        String query = "insert into MENTORING(mentoringCount, majorCategoryId, mentoringMentos, mentoringMentoId, mentoringMentiId) values(?,?,?,?,?)";
        Object[] params = new Object[]{postMentoringReq.getMentoringCount(), postMentoringReq.getMajorCategoryId(), postMentoringReq.getMentos(), postMentoringReq.getMentoId(), postMentoringReq.getMentiId()};
        this.jdbcTemplate.update(query, params);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    //멘토링 중복 확인
    public int checkMentoring(PostMentoringReq postMentoringReq){
        String query = "select exists (select mentoringId from MENTORING where mentoringmentoId=? and mentoringmentiId=? and majorCategoryId=? and mentoringStatus=0)";
        Object[] params = new Object[]{postMentoringReq.getMentoId(), postMentoringReq.getMentiId(), postMentoringReq.getMajorCategoryId()};
        return this.jdbcTemplate.queryForObject(query, int.class, params);
    }

    //멘토링 진행 중인 상태인지 확인
    public int checkProceedingMentoring(int mentoId, int mentiId, int categoryId){
        String query = "select mentoringId from MENTORING where mentoringMentoId=? and mentoringMentiId=? and majorCategoryId=? and mentoringStatus=1";
        Object[] params = new Object[]{mentoId, mentiId, categoryId};
        return this.jdbcTemplate.queryForObject(query, int.class, params);
    }

    //멘토가 멘토링 수락/거절 시 유효 멘토링 존재 여부 확인
    public int checkMentoringByMento(int mentoringId, int mentoId){
        String query = "select exists (select mentoringId from MENTORING where mentoringId=? and mentoringMentoId=? and mentoringStatus=0)";
        Object[] params = new Object[]{mentoringId, mentoId};
        return this.jdbcTemplate.queryForObject(query, int.class, params);
    }

    //멘토링 요청 수락
    public int acceptMentoring(int mentoringId){
        String query = "update MENTORING set mentoringStatus=1 where mentoringId=?";
        int param = mentoringId;
        return this.jdbcTemplate.update(query, param);
    }

    //멘토링 요청 거절
    public int deleteMentoring(int mentoringId){
        String query = "delete from MENTORING where mentoringId=?";
        int param = mentoringId;
        return this.jdbcTemplate.update(query, param);
    }

    //멘티가 멘토링 강제 종료시 유효 멘토링 존재 여부 확인
    public int checkMentoringByMenti(int mentoringId, int mentiId, int status){
        String query = "select exists (select mentoringId from MENTORING where mentoringId=? and mentoringMentiId=? and mentoringStatus=?)";
        Object[] params = new Object[]{mentoringId, mentiId, status};
        return this.jdbcTemplate.queryForObject(query, int.class, params);
    }

    //멘토링 강제 종료
    public int stopMentoring(int mentoringId){
        String query = "update MENTORING set mentoringStatus=2 where mentoringId=?";
        int param = mentoringId;
        return this.jdbcTemplate.update(query, param);
    }

    //멘토링 정보
    public Mentoring getMentoring(int mentoringId){
        String query = "select * from MENTORING where mentoringId = ?";
        int param = mentoringId;
        return this.jdbcTemplate.queryForObject(query,
                ((rs, rowNum) -> new Mentoring(
                        rs.getInt("mentoringId"),
                        rs.getInt("mentoringCount"),
                        rs.getInt("majorCategoryId"),
                        rs.getInt("mentoringMentos"),
                        rs.getInt("mentoringMentoId"),
                        rs.getInt("mentoringMentiId"),
                        rs.getInt("mentoringStatus"),
                        rs.getTimestamp("mentoringCreateAt"),
                        rs.getTimestamp("mentoringUpdateAt")
                )),
                param);
    }

    //멘토링 추가 진행
    public int addMentoring(Mentoring mentoring, int mentoringId){
        String query = "update MENTORING set mentoringCount = mentoringCount+?, mentoringMentos = mentoringMentos+? where mentoringId=?";
        Object[] params = new Object[]{mentoring.getMentoringCount(), mentoring.getMentoringMentos(), mentoringId};
        return this.jdbcTemplate.update(query, params);
    }
}
