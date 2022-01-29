package MentosServer.mentos.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class JwtRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void createRefreshToken(int memberId,String refreshToken){
        String insertQuery = "insert into JWT (memberId, refreshToken) VALUES (?,?)";
        Object[] params = new Object[]{memberId,refreshToken};
        this.jdbcTemplate.update(insertQuery,params);
    }
    public void updateRefreshToken(int memberId, String refreshToken){
        String updateQuery = "update JWT set refreshToken = ?,updateAt = CURRENT_TIMESTAMP() where memberId=?";
        Object[] params = new Object[]{refreshToken,memberId};
        this.jdbcTemplate.update(updateQuery,params);
    }
    //로그아웃
    public void deleteRefreshToken(int memberId){
        String deleteQuery = "update JWT set refreshToken= null,updateAt = CURRENT_TIMESTAMP()where memberId=?";
        this.jdbcTemplate.update(deleteQuery,memberId);
    }
    //멤버Id 얻어오기
    public int getMemberId(String refreshToken){
        String getQuery = "select memberId from JWT where refreshToken=?";
        return this.jdbcTemplate.queryForObject(getQuery,int.class,refreshToken);
    }
}
