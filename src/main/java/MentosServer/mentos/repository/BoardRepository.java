package MentosServer.mentos.repository;

import MentosServer.mentos.model.domain.Post;
import MentosServer.mentos.model.domain.PostImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class BoardRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //--------Post 테이블---------------
    //게시글 등록
    public int createPost(Post post) {
        String postPostsQuery = "insert into POST (majorCategoryId, memberId, postTitle, postContents) VALUES (?,?,?,?)";
        Object[] postParams = new Object[]{post.getMajorCategoryId(),post.getMemberId(),post.getPostTitle(),post.getPostContents()};
        this.jdbcTemplate.update(postPostsQuery,postParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    //게시글 수정
    public void patchPost(Post post) {
        String patchPostQuery = "update POST set majorCategoryId=?, postTitle=?,postContents=?,postUpdateAt=CURRENT_TIMESTAMP() where postId=?";
        Object[] patchParams = new Object[]{post.getMajorCategoryId(),post.getPostTitle(),post.getPostContents(),post.getPostId()};
        this.jdbcTemplate.update(patchPostQuery,patchParams);
    }

    //작성자와 요청자가 동일한지 확인
    public int checkPostMember(int postId) {
        String checkPostQuery = "select memberId from POST where postId = ?";
        return this.jdbcTemplate.queryForObject(checkPostQuery,Integer.class,postId);
    }

    //게시글 삭제
    public void deletePost(int postId) {
        String deletePostQuery = "delete from POST where postId =?";
        this.jdbcTemplate.update(deletePostQuery,postId);
    }
//--------Image 테이블---------------

    //저장되어있는 이미지 url을 가져오는 메소드
    public Optional<String> getImageUrl(int postId) {
        String getImgUrlQuery = "select if(count(*)<=0,null,imageUrl) as imageUrl from image where postId = ?";
        return Optional.ofNullable(this.jdbcTemplate.queryForObject(getImgUrlQuery,String.class,postId));
    }

    //이미지 업로드
    public void uploadPostImage(PostImage postImage) {
        String insertImageQuery = "insert into IMAGE (postId,imageUrl) VALUES(?,?)";
        Object[] postParam = new Object[]{postImage.getPostId(),postImage.getImageUrl()};
        this.jdbcTemplate.update(insertImageQuery,postParam);
    }
    //이미지 삭제
    public void deletePostImage(int postId) {
        String deleteImgQuery = "delete from IMAGE where postId = ?";
        this.jdbcTemplate.update(deleteImgQuery,postId);
    }

}
