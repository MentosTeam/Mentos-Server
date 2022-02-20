package MentosServer.mentos.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FcmTokenRespository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int insertNewUserDeviceToken(int memberId, String fcmToken) {
        String query = "insert into userdevice (memberId,deviceToken) values (?,?)";
        Object[] params = new Object[]{memberId, fcmToken};
        return jdbcTemplate.update(query,params);
    }

    public List<String> selectUserDeviceTokenByIdx(int userIdx) {
        List<String> tokenList = new ArrayList<>();
        String query ="select deviceToken from userdevice where memberId=? and deleteFlag=0";
        jdbcTemplate.query(query,(rs,rowNum)->tokenList.add(rs.getString("deviceToken")),userIdx);
        return tokenList;
    }

    public int deleteUserDeviceToken(int memberId, String fcmToken) {
        String query = "update userdevice set deleteFlag=1 where memberId=? and deviceToken=?"; //하나 삭제 처리
        Object[] params = new Object[]{memberId, fcmToken};
        return jdbcTemplate.update(query,params);
    }

    public int updateUserDeviceToken(int memberId, String oldFcmToken, String newFcmToken) {
        String query="update userdevice set deviceToken =? where deviceToken=? and memberId=?";
        Object[] params = new Object[]{newFcmToken,oldFcmToken,memberId};
        return jdbcTemplate.update(query,params);
    }

    public int deleteUserAllDeviceToken(int memberId) {
        String query = "update userdevice set deleteFlag=1 where memberId=?";
        return jdbcTemplate.update(query,memberId);
    }

    public int existDeviceToken(int memberId, String fcmToken) {
        String query = "select exists(select * from userdevice where memberId=? and deviceToken=?)";
        Object[] params = new Object[]{memberId,fcmToken};
        return jdbcTemplate.queryForObject(query,int.class,params);
    }
}
