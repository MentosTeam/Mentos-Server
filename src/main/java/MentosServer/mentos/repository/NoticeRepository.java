package MentosServer.mentos.repository;

import MentosServer.mentos.model.dto.GetNotificationRes;
import MentosServer.mentos.model.dto.GetNoticeRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class NoticeRepository {

    private JdbcTemplate jdbcTemplate;


    @Autowired
    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 전체 공지 조회
    public List<GetNoticeRes> getNoticeList() {
        String getNoticeQuery = "select * from Notice order by noticeId desc"; //User 테이블에 존재하는 모든 회원들의 정보를 조회하는 쿼리
        return this.jdbcTemplate.query(getNoticeQuery,
                (rs, rowNum) -> new GetNoticeRes(
                        rs.getInt("noticeId"),
                        rs.getString("content"),
                        rs.getTimestamp("createAt"))

        );
    }

    public GetNoticeRes getNotice(int noticeId) {
        String getNoticeQuery = "select * from Notice where noticeId = ?"; // 해당 noticeId를 조회하는 쿼리문
        int getNoticeParams = noticeId;
        return this.jdbcTemplate.queryForObject(getNoticeQuery,
                (rs, rowNum) -> new GetNoticeRes(
                        rs.getInt("noticeId"),
                        rs.getString("content"),
                        rs.getTimestamp("createAt")),
                getNoticeParams); // 한 개의 회원정보를 얻기 위한 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    public int setNotification(int memberId, int statusFlag, String content){
        String query ="insert into notification(memberId,statusFlag,content) values(?,?,?)";
        Object[] params = new Object[]{memberId,statusFlag,content};
        return this.jdbcTemplate.update(query,params);
    }

    public List<GetNotificationRes> getNotification(int memberId, int statusFlag){
        try {
            String query = "select *, date_format(updateAt,\"%Y-%m-%d\") as aa from notification where memberId=? and statusFlag=? order by notificationId desc";
            Object[] params = new Object[]{memberId, statusFlag};
            return this.jdbcTemplate.query(query, (rs, rowNum) -> new GetNotificationRes(
                    rs.getInt("notificationId"),
                    rs.getInt("memberId"),
                    rs.getInt("statusFlag"),
                    rs.getString("content"),
                    rs.getString("aa")
                    )
                    , params);
        }catch (EmptyResultDataAccessException e){
            return null;
        }

    }

}
