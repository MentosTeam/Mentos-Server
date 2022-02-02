package MentosServer.mentos.repository;

import MentosServer.mentos.model.dto.GetMenteeProfileRes;
import MentosServer.mentos.model.dto.ProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class MenteeProfileRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //멘티 프로필 기본 정보 조회
    public ProfileDto getBasicInfoByMentee(int memberId){
        String query = "select memberId, memberName, memberNickName, memberStudentId, memberSchoolId, memberMajor, memberSex, mentiMajorFirst, mentiMajorSecond, mentiImage, mentiIntro from MEMBER natural join MENTI where memberId = ?";
        int param = memberId;
        return this.jdbcTemplate.queryForObject(query,
                (rs, rowNum) -> new ProfileDto(
                        rs.getInt("memberId"),
                        rs.getString("memberName"),
                        rs.getString("memberNickName"),
                        rs.getInt("memberStudentId"),
                        rs.getInt("memberSchoolId"),
                        rs.getString("memberMajor"),
                        rs.getString("memberSex"),
                        rs.getInt("mentiMajorFirst"),
                        rs.getInt("mentiMajorSecond"),
                        rs.getString("mentiImage"),
                        rs.getString("mentiIntro")
                ), param);
    }

    //멘티 프로필 생성 여부 확인
    public int checkMenteeProfile(int memberId){
       String query = "select exists (select memberId from MENTI where memberId = ?)";
       int param = memberId;
       return this.jdbcTemplate.queryForObject(query, int.class, param);
    }

    //학교명 조회
    public String getSchoolName(int schoolId){
        String query = "select schoolName from SCHOOLCATEGORY where schoolId=?";
        int param = schoolId;
        return this.jdbcTemplate.queryForObject(query, String.class, param);
    }

    //멘티 기준 종료된 멘토링 횟수 조회
    public int getNumOfMentoringByMentee(int memberId){
        String query = "select count(mentoringId) from MENTORING where mentoringMentiId = ? and mentoringStatus = 2";
        int param = memberId;
        return this.jdbcTemplate.queryForObject(query, int.class, param);
    }
}
