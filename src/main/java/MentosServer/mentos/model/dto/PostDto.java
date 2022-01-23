package MentosServer.mentos.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostDto {
	
	private String postId;
	
	private String memberId;
	
	private String postTitle;
	
	private String postContents;
	
	private List<String> imageUrls;
	
	public PostDto(String postId, String memberId, String postTitle, String postContents) {
		this.postId = postId;
		this.memberId = memberId;
		this.postTitle = postTitle;
		this.postContents = postContents;
	}
}
