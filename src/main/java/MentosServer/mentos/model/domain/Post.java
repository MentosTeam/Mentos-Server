package MentosServer.mentos.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class Post implements Comparable<Post>{
	
	private int postId;
	
	private int majorCategoryId;
	
	private int memberId;
	
	private String postTitle;
	
	private String postContents;
	
	private Timestamp postCreateAt;
	
	private Timestamp postUpdateAt;
	
	/**
	 * 정렬을 위한 method
	 * @param post
	 * @return
	 */
	@Override
	public int compareTo(Post post) {
		if(getPostUpdateAt().toLocalDateTime().isAfter(post.getPostUpdateAt().toLocalDateTime())) return 1;
		else return 0;
	}
}
