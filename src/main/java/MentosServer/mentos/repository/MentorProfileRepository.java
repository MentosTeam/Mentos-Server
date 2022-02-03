package MentosServer.mentos.repository;

import MentosServer.mentos.model.domain.Post;
import MentosServer.mentos.model.domain.Review;
import MentosServer.mentos.model.dto.MentorProfileDto;
import MentosServer.mentos.model.dto.MentosCountDto;
import MentosServer.mentos.model.dto.PostWithImageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MentorProfileRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //멘토 프로필 존재 여부 확인
    public int checkMentorProfile(int memberId){
        String query = "select exists (select memberId from MENTO where memberId = ?)";
        int param = memberId;
        return this.jdbcTemplate.queryForObject(query, int.class, param);
    }

    //멘토 기본 정보 조회
    public MentorProfileDto getBasicInfoByMentor(int memberId){
        String query = "select memberId, memberName, memberNickName, memberStudentId, memberSchoolId, memberMajor, memberSex, mentoMajorFirst, mentoMajorSecond, mentoImage, mentoIntro, mentoScore from MEMBER natural join MENTO where memberId = ?";
        int param = memberId;
        return this.jdbcTemplate.queryForObject(query,
                (rs, rowNum) -> new MentorProfileDto(
                        rs.getInt("memberId"),
                        rs.getString("memberName"),
                        rs.getString("memberNickName"),
                        rs.getInt("memberStudentId"),
                        rs.getInt("memberSchoolId"),
                        rs.getString("memberMajor"),
                        rs.getString("memberSex"),
                        rs.getInt("mentoMajorFirst"),
                        rs.getInt("mentoMajorSecond"),
                        rs.getString("mentoImage"),
                        rs.getString("mentoIntro"),
                        rs.getDouble("mentoScore")
                ), param);
    }

    //학교명 조회
    public String getSchoolName(int schoolId){
        String query = "select schoolName from SCHOOLCATEGORY where schoolId=?";
        int param = schoolId;
        return this.jdbcTemplate.queryForObject(query, String.class, param);
    }

    //멘토 기준 종료된 멘토링 횟수 조회
    public int getNumOfMentoringByMentor(int memberId){
        String query = "select count(mentoringId) from MENTORING where mentoringMentoId = ? and mentoringStatus = 2";
        int param = memberId;
        return this.jdbcTemplate.queryForObject(query, int.class, param);
    }

    //멘토 작성 post 조회
    public List<PostWithImageDto> getPostsByMemberId(int memberId){
        String query = "select postId,memberId,majorCategoryId,postTitle,postContents, imageUrl from POST left outer join IMAGE using (postId) where memberId = ?";
        int param = memberId;
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new PostWithImageDto(
                        rs.getInt("postId"),
                        rs.getInt("memberId"),
                        rs.getInt("majorCategoryId"),
                        rs.getString("postTitle"),
                        rs.getString("postContents"),
                        rs.getString("imageUrl")
                ),
                param);
    }

    //멘토에 대한 review 조회
    public List<Review> getReviewsOnMentor(int memberId){
        String query = "select reviewId, mentoringId, reviewText, reviewScore from REVIEW natural join MENTORING where mentoringMentoId = ?";
        int param = memberId;
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new Review(
                        rs.getInt("reviewId"),
                        rs.getInt("mentoringId"),
                        rs.getString("reviewText"),
                        rs.getDouble("reviewScore")
                ),
                param);
    }

    //지금까지 진행한 멘토링(멘토쓰 카테고리+멘토쓰 개수)
    public List<MentosCountDto> getMentosCount(int memberId){
        String query = "select mentoringId, majorCategoryId, mentoringMentos from MENTORING where mentoringMentoId = ? and mentoringStatus = 2";
        int param = memberId;
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new MentosCountDto(
                        rs.getInt("mentoringId"),
                        rs.getInt("majorCategoryId"),
                        rs.getInt("mentoringMentos")
                ),
                param);
    }
}
