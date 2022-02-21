package MentosServer.mentos.model.dto;

import lombok.Data;

@Data
public class PostDto {
	
	private int postId;
	
	private int majorCategoryId;
	
	private int mentoId;
	
	private String memberMajor;
	
	private String mentoNickName;
	
	private String mentoImage;
	
	private String postTitle;
	
	private String postContents;
	
	private String imageUrl;
	
	public PostDto(int postId, int majorCategoryId, int mentoId, String memberMajor, String mentoNickName, String mentoImage, String postTitle, String postContents) {
		this.postId = postId;
		this.majorCategoryId = majorCategoryId;
		this.mentoId = mentoId;
		this.memberMajor = memberMajor;
		this.mentoNickName = mentoNickName;
		this.mentoImage = mentoImage;
		this.postTitle = postTitle;
		this.postContents = postContents;
	}
}
