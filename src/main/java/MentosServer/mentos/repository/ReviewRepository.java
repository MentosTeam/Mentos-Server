package MentosServer.mentos.repository;

import MentosServer.mentos.model.domain.Member;
import MentosServer.mentos.model.dto.PostLoginReq;
import MentosServer.mentos.model.dto.PostReviewReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ReviewRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }




    // 리뷰 등록되어있는지 아닌지 확인
    public int checkReview(int mentoringId) {
        String checkMentoringQuery = "select exists(select mentoringId from review where mentoringId = ?)"; // Review Table에 해당 mentoringId 값을 갖는 멘토링 정보가 존재하는가?
        int checkMentoringParams = mentoringId; // 해당(확인할) 이메일 값
        return this.jdbcTemplate.queryForObject(checkMentoringQuery,
                int.class,
                checkMentoringParams); // checkMentoringQuery, checkMentoringParams를 통해 가져온 값(intgud)을 반환한다. -> 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
    }

    // 리뷰 등록
    public int createReview(PostReviewReq postReviewReq) {
        String createReviewQuery = "insert into Review (mentoringId, reviewText, reviewScore) VALUES (?,?,?)"; // 실행될 동적 쿼리문
        Object[] createReviewParams = new Object[]{postReviewReq.getMentoringId(), postReviewReq.getReviewText(), postReviewReq.getReviewScore()}; // 동적 쿼리의 ?부분에 주입될 값
        this.jdbcTemplate.update(createReviewQuery, createReviewParams);

        String lastInserIdQuery = "select last_insert_id()"; // 가장 마지막에 삽입된(생성된) id값은 가져온다.
        return this.jdbcTemplate.queryForObject(lastInserIdQuery, int.class); // 해당 쿼리문의 결과 마지막으로 삽인된 유저의 userIdx번호를 반환한다.
    }

    // 멘토 평점 추가
    public int sumScore(int mentoringId, double mentoScore) {

        String MentorSQL ="select mentoringMentoId from mentoring where mentoringId = ?";
        int mentorId = this.jdbcTemplate.queryForObject(MentorSQL,int.class, mentoringId);


        String updateScoreQuery = "update mento set mentoScore = mentoScore + ? where memberId = ?"; // 실행될 동적 쿼리문
        //Object[] createReviewParams = new Object[]{mentoScore, memberId}; // 동적 쿼리의 ?부분에 주입될 값
        return this.jdbcTemplate.update(updateScoreQuery, mentoScore, mentorId);
    }

    // 멘티 멘토스 받기
    public int updateMentos(int memberId,int mentoringMentos) {
        String updateMentosQuery = "update member set memberMentos = memberMentos + ? where memberId = ?"; // 실행될 동적 쿼리문
        //Object[] createMentosParams = new Object[]{mentoringMentos, memberId}; // 동적 쿼리의 ?부분에 주입될 값
        return this.jdbcTemplate.update(updateMentosQuery, mentoringMentos, memberId);
    }





}
