package MentosServer.mentos.repository;

import MentosServer.mentos.model.dto.SignUpReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class SignUpRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createMember(SignUpReq signUpReq) {
        // 회원가입
        String createUserQuery = "insert into member (memberName,memberNickName,memberStudentId,memberEmail,memberPw,memberSchoolId,memberSex) VALUES (?,?,?,?,?,(select schoolId from schoolCategory where schoolName=?),?)"; // 실행될 동적 쿼리문
        Object[] createUserParams = new Object[]{signUpReq.getMemberName(),signUpReq.getMemberNickName(),signUpReq.getMemberStudentId(),signUpReq.getMemberEmail(),signUpReq.getMemberPw(),signUpReq.getMemberSchoolName(),signUpReq.getMemberSex()}; // 동적 쿼리의 ?부분에 주입될 값
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        //last_insert_id 대신 쓰는 쿼리문 찾기
        String lastInsertIdQuery = "select last_insert_id()"; // 가장 마지막에 삽입된(생성된) id값은 가져온다.
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class); // 해당 쿼리문의 결과 마지막으로 삽인된 유저의 userIdx번호를 반환한다
    }

    public int checkNickName(String nickName) {
        String checkNickNameQuery = "select exists(select memberId from member where memberNickName = ? and memberStatus='active')"; // memberTable에 해당 닉네임 값을 갖는 유저 정보가 존재하는가?
        return this.jdbcTemplate.queryForObject(checkNickNameQuery,
                int.class,
                nickName); // checkPhoneNumQuery, checkPhoneNumParams를 통해 가져온 값(intgud)을 반환한다. -> 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
    }

    public int checkEmail(String memberEmail) {
        String checkNickNameQuery = "select exists(select memberId from member where memberEmail = ? and memberStatus='active')"; // User Table에 해당 email 값을 갖는 유저 정보가 존재하는가?
        return this.jdbcTemplate.queryForObject(checkNickNameQuery,
                int.class,
                memberEmail);
    }
}
