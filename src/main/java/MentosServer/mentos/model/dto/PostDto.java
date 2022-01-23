package MentosServer.mentos.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostDto {
	
	private String postId;
	
	private String majorCategoryId;
	
	private String mentoId;
	
	private String mentoImage;
	
	private String mentoNickName;
	
	private String postTitle;
	
	private String postContents;
	
	private List<String> imageUrls;
	
	
	public PostDto(String postId, String majorCategoryId, String mentoId, String mentoImage, String mentoNickName, String postTitle, String postContents) {
		this.postId = postId;
		this.majorCategoryId = majorCategoryId;
		this.mentoId = mentoId;
		this.mentoImage = mentoImage;
		this.mentoNickName = mentoNickName;
		this.postTitle = postTitle;
		this.postContents = postContents;
	}
}
