package MentosServer.mentos.model.dto;

import lombok.Data;


@Data
public class PostDto {
	
	private int postId;
	
	private int majorCategoryId;
	
	private int mentoId;
	
	private String memberMajor;
	
	private String mentoNickName;
	
	private String postTitle;
	
	private String postContents;
	
	private String imageUrl;
	
	public PostDto(int postId, int majorCategoryId, int mentoId, String memberMajor, String mentoNickName, String postTitle, String postContents) {
		this.postId = postId;
		this.majorCategoryId = majorCategoryId;
		this.mentoId = mentoId;
		this.memberMajor = memberMajor;
		this.mentoNickName = mentoNickName;
		this.postTitle = postTitle;
		this.postContents = postContents;
	}
}
