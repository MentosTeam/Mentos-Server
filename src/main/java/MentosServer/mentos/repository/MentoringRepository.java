package MentosServer.mentos.repository;

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
}
