package MentosServer.mentos.repository;
import MentosServer.mentos.model.dto.EndMentoringRes;

import MentosServer.mentos.model.dto.NowMentoringRes;
import MentosServer.mentos.model.dto.MentoringStatusRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MentoringStatusRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    // 해당 멘토의 현재멘토링 정보 조회
    public List<NowMentoringRes> getMentorMentoringNowList(int memberId) {
        try{
            String query="select mt.mentoringId as mentoringId,mentoringCount,majorCategoryId,mentoringMentos,\n" +
                    "m.memberNickName as mentorName,\n" +
                    "(select memberNickName from member where memberId=mt.mentoringmentiId) as menteeName,\n" +
                    "count(reportId) as currentCount \n" +
                    "from mentoring as mt \n" +
                    "inner join member as m on mt.mentoringMentoId=m.memberId \n" +
                    "left join report as r on mt.mentoringId=r.mentoringId \n" +
                    "where mt.mentoringMentoId=? and mt.mentoringStatus=1 and mt.mentoringMentiId in (select memberId from member where memberStatus='active')\n" +
                    "group by mt.mentoringId\n"+
                    "order by mt.mentoringCreateAt desc";

            int getMemberIdParams = memberId;
            return this.jdbcTemplate.query(query,
                    (rs, rowNum) -> new NowMentoringRes(
                            rs.getInt("mentoringId"),
                            rs.getInt("mentoringCount"),
                            rs.getInt("majorCategoryId"),
                            rs.getInt("mentoringMentos"),
                            rs.getString("mentorName"),
                            rs.getString("menteeName"),
                            rs.getInt("currentCount")
                    ), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                    getMemberIdParams);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    // 해당 멘토의 멘토링 정보 조회

    public List<EndMentoringRes> getMentorMentoringEndList(int memberId) {

        try {
            String getMentoringQuery = "select mt.mentoringId,mentoringCount,majorCategoryId,mentoringMentos,mentoringStatus,\n" +
                    "m.memberNickName as mentorName,\n" +
                    "(select memberNickName from member where memberId=mt.mentoringmentiId) as menteeName,\n" +
                    "count(reviewId) as reviewCheck\n" +
                    "from mentoring as mt \n" +
                    "inner join member as m on mt.mentoringMentoId=m.memberId\n" +
                    "left join review as r on mt.mentoringId=r.mentoringId\n" +
                    "where mt.mentoringMentoId=? and mentoringStatus=2 and mt.mentoringMentiId in (select memberId from member where memberStatus='active')\n" +
                    "group by mt.mentoringId\n" +
                    "order by mt.mentoringCreateAt desc";
            int getMemberIdParams = memberId;
            return this.jdbcTemplate.query(getMentoringQuery,
                    (rs, rowNum) -> new EndMentoringRes(
                            rs.getInt("mentoringId"),
                            rs.getInt("mentoringCount"),
                            rs.getInt("majorCategoryId"),
                            rs.getInt("mentoringMentos"),
                            rs.getInt("reviewCheck"), //0이면 존재x 1이면 존재
                            rs.getString("mentorName"),
                            rs.getString("menteeName")

                    ), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                    getMemberIdParams);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    // 해당 멘토의 신청 대기 멘토링 정보 조회
    public List<MentoringStatusRes> getMentorMentoringWaitList(int memberId) {
        try {
            String getMentoringQuery = "select mt.mentoringId,mentoringCount,majorCategoryId,mentoringMentos,mentoringStatus,\n" +
                    "m.memberNickName as mentorName,\n" +
                    "(select memberNickName from member where memberId=mt.mentoringmentiId) as menteeName\n" +
                    "from mentoring as mt \n" +
                    "inner join member as m on mt.mentoringMentoId=m.memberId\n" +
                    "where mt.mentoringMentoId=? and mentoringStatus=0 and mt.mentoringMentiId in (select memberId from member where memberStatus='active')\n" +
                    "group by mt.mentoringId\n" +
                    "order by mt.mentoringCreateAt desc";
            int getMemberIdParams = memberId;


            return this.jdbcTemplate.query(getMentoringQuery,
                    (rs, rowNum) -> new MentoringStatusRes(
                            rs.getInt("mentoringId"),
                            rs.getInt("mentoringCount"),
                            rs.getInt("majorCategoryId"),
                            rs.getInt("mentoringMentos"),
                            rs.getString("mentorName"),
                            rs.getString("menteeName")
                    ),
                    getMemberIdParams); // 해당 멤버값을 갖는 모든 멘토링 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }


    // 해당 멘티의 현재 멘토링 정보 조회
    public List<NowMentoringRes> getMenteeMentoringNowList(int memberId) {
        try{
        String query="select mt.mentoringId as mentoringId,mentoringCount,majorCategoryId,mentoringMentos,\n" +
                "m.memberNickName as menteeName,\n" +
                "(select memberNickName from member where memberId=mt.mentoringmentoId) as mentorName,\n" +
                "count(reportId) as currentCount \n" +
                "from mentoring as mt \n" +
                "inner join member as m on mt.mentoringMentiId=m.memberId \n" +
                "left join report as r on mt.mentoringId=r.mentoringId \n" +
                "where mt.mentoringMentiId=? and mt.mentoringStatus=1 and mt.mentoringMentoId in (select memberId from member where memberStatus='active')\n" +
                "group by mt.mentoringId\n"+
                "order by mt.mentoringCreateAt desc";

        int getMemberIdParams = memberId;
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new NowMentoringRes(
                        rs.getInt("mentoringId"),
                        rs.getInt("mentoringCount"),
                        rs.getInt("majorCategoryId"),
                        rs.getInt("mentoringMentos"),
                        rs.getString("mentorName"),
                        rs.getString("menteeName"),
                        rs.getInt("currentCount")
                ), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getMemberIdParams);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    // 해당 멘티의 멘토링 정보 조회
    public List<EndMentoringRes> getMenteeMentoringEndList(int memberId) {
    try {
        String getMentoringQuery = "select mt.mentoringId,mentoringCount,majorCategoryId,mentoringMentos,\n" +
                "m.memberNickName as menteeName,\n" +
                "(select memberNickName from member where memberId=mt.mentoringmentoId) as mentorName,\n" +
                "count(reviewId) as reviewCheck\n" +
                "from mentoring as mt \n" +
                "inner join member as m on mt.mentoringMentiId=m.memberId\n" +
                "left join review as r on mt.mentoringId=r.mentoringId\n" +
                "where mt.mentoringMentiId=? and mentoringStatus=2 and mt.mentoringMentoId in (select memberId from member where memberStatus='active')\n" +
                "group by mt.mentoringId\n" +
                "order by mt.mentoringCreateAt desc";
        int getMemberIdParams = memberId;
        return this.jdbcTemplate.query(getMentoringQuery,
            (rs, rowNum) -> new EndMentoringRes(
                    rs.getInt("mt.mentoringId"),
                    rs.getInt("mentoringCount"),
                    rs.getInt("majorCategoryId"),
                    rs.getInt("mentoringMentos"),
                    rs.getInt("reviewCheck"), //0이면 존재x 1이면 존재
                    rs.getString("mentorName"),
                    rs.getString("menteeName")

            ), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
            getMemberIdParams);
    }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    // 해당 멘티의 멘토링 정보 조회
    public List<MentoringStatusRes> getMenteeMentoringWaitList(int memberId) {
        try{
        String getMentoringQuery = "select mt.mentoringId,mentoringCount,majorCategoryId,mentoringMentos,\n" +
                "m.memberNickName as menteeName,\n" +
                "(select memberNickName from member where memberId=mt.mentoringmentoId) as mentorName\n" +
                "from mentoring as mt \n" +
                "inner join member as m on mt.mentoringMentiId=m.memberId\n" +
                "where mt.mentoringMentiId=? and mentoringStatus=0 and mt.mentoringMentoId in (select memberId from member where memberStatus='active')\n" +
                "group by mt.mentoringId \n"+
                "order by mt.mentoringCreateAt desc";

        int getMemberIdParams = memberId;
        return this.jdbcTemplate.query(getMentoringQuery,
                (rs, rowNum) -> new MentoringStatusRes(
                        rs.getInt("mt.mentoringId"),
                        rs.getInt("mentoringCount"),
                        rs.getInt("majorCategoryId"),
                        rs.getInt("mentoringMentos"),
                        rs.getString("mentorName"),
                        rs.getString("menteeName")
                ), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getMemberIdParams);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }


}
