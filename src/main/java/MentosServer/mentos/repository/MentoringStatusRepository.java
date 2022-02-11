package MentosServer.mentos.repository;


import MentosServer.mentos.model.dto.EndMentoringRes;
import MentosServer.mentos.model.dto.MentoringStatusRes;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<MentoringStatusRes> getMentorMentoringNowList(int memberId) {
        String MentorSQL ="select memberName from member where memberId = ?";
        String mentoringMentorName = this.jdbcTemplate.queryForObject(MentorSQL,String.class, memberId);

        String MenteeIdSQL ="select mentoringMentiId from mentoring where mentoringMentoId = ?";
        int MenteeId = this.jdbcTemplate.queryForObject(MenteeIdSQL,Integer.class, memberId);

        String MenteeSQL ="select memberName from member where memberId = ?";
        String mentoringMenteeName = this.jdbcTemplate.queryForObject(MenteeSQL,String.class, MenteeId);



        String getMentoringQuery = "select mentoringId, mentoringCount, majorCategoryId, mentoringMentos from mentoring where mentoringMentoId = ? and mentoringStatus = 1"; // 해당 멤버를 만족하는 멘토링을 조회하는 쿼리문

        int getMemberIdParams = memberId;


        return this.jdbcTemplate.query(getMentoringQuery,
                (rs, rowNum) -> new MentoringStatusRes(
                        rs.getInt("mentoringId"),
                        rs.getInt("mentoringCount"),
                        rs.getInt("majorCategoryId"),
                        rs.getInt("mentoringMentos"),
                        mentoringMentorName,
                        mentoringMenteeName
                        ),
                getMemberIdParams); // 해당 멤버값을 갖는 모든 멘토링 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    // 해당 멘토의 멘토링 정보 조회

    public List<EndMentoringRes> getMentorMentoringEndList(int memberId) {
        String MentorSQL ="select memberName from member where memberId = ?";
        String mentoringMentorName = this.jdbcTemplate.queryForObject(MentorSQL,String.class, memberId);

        String MenteeIdSQL ="select mentoringMentiId from mentoring where mentoringMentoId = ?";
        int MenteeId = this.jdbcTemplate.queryForObject(MenteeIdSQL,Integer.class, memberId);

        String MenteeSQL ="select memberName from member where memberId = ?";
        String mentoringMenteeName = this.jdbcTemplate.queryForObject(MenteeSQL,String.class, MenteeId);


        String getMentoringQuery = "select mentoring.mentoringId, mentoringCount, majorCategoryId, mentoringMentos, count(review.mentoringId) AS reviewCheck from mentoring INNER JOIN review ON review.mentoringId=Mentoring.mentoringId where mentoringMentoId = ? and mentoringStatus = 2";
                //"select mentoringId, mentoringCount, majorCategoryId, mentoringMentos from mentoring where mentoringMentoId = ? and mentoringStatus=2"; // 해당 멤버를 만족하는 멘토링을 조회하는 쿼리문
        int getMemberIdParams = memberId;


        return this.jdbcTemplate.query(getMentoringQuery,
                (rs, rowNum) -> new EndMentoringRes(
                        rs.getInt("mentoringId"),
                        rs.getInt("mentoringCount"),
                        rs.getInt("majorCategoryId"),
                        rs.getInt("mentoringMentos"),
                        rs.getInt("reviewCheck"),
                        mentoringMentorName,
                        mentoringMenteeName

                ),
                getMemberIdParams); // 해당 멤버값을 갖는 모든 멘토링 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    // 해당 멘토의 현재멘토링 정보 조회
    public List<MentoringStatusRes> getMentorMentoringWaitList(int memberId) {
        String MentorSQL ="select memberName from member where memberId = ?";
        String mentoringMentorName = this.jdbcTemplate.queryForObject(MentorSQL,String.class, memberId);

        String MenteeIdSQL ="select mentoringMentiId from mentoring where mentoringMentoId = ?";
        int MenteeId = this.jdbcTemplate.queryForObject(MenteeIdSQL,Integer.class, memberId);

        String MenteeSQL ="select memberName from member where memberId = ?";
        String mentoringMenteeName = this.jdbcTemplate.queryForObject(MenteeSQL,String.class, MenteeId);



        String getMentoringQuery = "select mentoringId, mentoringCount, majorCategoryId, mentoringMentos from mentoring where mentoringMentoId = ? and mentoringStatus = 0"; // 해당 멤버를 만족하는 멘토링을 조회하는 쿼리문

        int getMemberIdParams = memberId;


        return this.jdbcTemplate.query(getMentoringQuery,
                (rs, rowNum) -> new MentoringStatusRes(
                        rs.getInt("mentoringId"),
                        rs.getInt("mentoringCount"),
                        rs.getInt("majorCategoryId"),
                        rs.getInt("mentoringMentos"),
                        mentoringMentorName,
                        mentoringMenteeName
                ),
                getMemberIdParams); // 해당 멤버값을 갖는 모든 멘토링 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }


    // 해당 멘티의 멘토링 정보 조회
    public List<MentoringStatusRes> getMenteeMentoringNowList(int memberId) {
        String MenteeSQL ="select memberName from member where memberId = ?";
        String mentoringMenteeName = this.jdbcTemplate.queryForObject(MenteeSQL,String.class, memberId);

        String MentorIdSQL ="select mentoringMentoId from mentoring where mentoringMentiId = ?";
        int MentorId = this.jdbcTemplate.queryForObject(MentorIdSQL,Integer.class, memberId);

        String MentorSQL ="select memberName from member where memberId = ?";
        String mentoringMentorName = this.jdbcTemplate.queryForObject(MentorSQL,String.class, MentorId);


        String getMentoringQuery = "select mentoringId, mentoringCount, majorCategoryId, mentoringMentos from mentoring where mentoringMentiId =? and mentoringStatus=1"; // 해당 멤버를 만족하는 멘토링을 조회하는 쿼리문

        int getMemberIdParams = memberId;
        return this.jdbcTemplate.query(getMentoringQuery,
                (rs, rowNum) -> new MentoringStatusRes(
                        rs.getInt("mentoringId"),
                        rs.getInt("mentoringCount"),
                        rs.getInt("majorCategoryId"),
                        rs.getInt("mentoringMentos"),
                        mentoringMentorName,
                        mentoringMenteeName
                ), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getMemberIdParams);
    }

    // 해당 멘티의 멘토링 정보 조회

    public List<EndMentoringRes> getMenteeMentoringEndList(int memberId) {
        String MenteeSQL ="select memberName from member where memberId = ?";
        String mentoringMenteeName = this.jdbcTemplate.queryForObject(MenteeSQL,String.class, memberId);

        String MentorIdSQL ="select mentoringMentoId from mentoring where mentoringMentiId = ?";
        int MentorId = this.jdbcTemplate.queryForObject(MentorIdSQL,Integer.class, memberId);

        String MentorSQL ="select memberName from member where memberId = ?";
        String mentoringMentorName = this.jdbcTemplate.queryForObject(MentorSQL,String.class, MentorId);



        String getMentoringQuery = "select Mentoring.mentoringId, mentoringCount, majorCategoryId, mentoringMentos, count(review.mentoringId) AS reviewCheck from MENTORING INNER JOIN review ON review.mentoringId=Mentoring.mentoringId where mentoringMentiId = ? and mentoringStatus = 2";

                //"select mentoringId, mentoringCount, majorCategoryId, mentoringMentos from mentoring where mentoringMentiId =? and mentoringStatus=2"; // 해당 멤버를 만족하는 멘토링을 조회하는 쿼리문
        int getMemberIdParams = memberId;
        return this.jdbcTemplate.query(getMentoringQuery,
                (rs, rowNum) -> new EndMentoringRes(
                        rs.getInt("mentoringId"),
                        rs.getInt("mentoringCount"),
                        rs.getInt("majorCategoryId"),
                        rs.getInt("mentoringMentos"),
                        rs.getInt("reviewCheck"),
                        mentoringMentorName,
                        mentoringMenteeName

                ), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getMemberIdParams);
    }

    // 해당 멘티의 멘토링 정보 조회
    public List<MentoringStatusRes> getMenteeMentoringWaitList(int memberId) {
        String MenteeSQL ="select memberName from member where memberId = ?";
        String mentoringMenteeName = this.jdbcTemplate.queryForObject(MenteeSQL,String.class, memberId);

        String MentorIdSQL ="select mentoringMentoId from mentoring where mentoringMentiId = ?";
        int MentorId = this.jdbcTemplate.queryForObject(MentorIdSQL,Integer.class, memberId);

        String MentorSQL ="select memberName from member where memberId = ?";
        String mentoringMentorName = this.jdbcTemplate.queryForObject(MentorSQL,String.class, MentorId);

        String getMentoringQuery = "select mentoringId, mentoringCount, majorCategoryId, mentoringMentos from mentoring where mentoringMentiId =? and mentoringStatus=0"; // 해당 멤버를 만족하는 멘토링을 조회하는 쿼리문

        int getMemberIdParams = memberId;
        return this.jdbcTemplate.query(getMentoringQuery,
                (rs, rowNum) -> new MentoringStatusRes(
                        rs.getInt("mentoringId"),
                        rs.getInt("mentoringCount"),
                        rs.getInt("majorCategoryId"),
                        rs.getInt("mentoringMentos"),
                        mentoringMentorName,
                        mentoringMenteeName
                ), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getMemberIdParams);
    }


}
