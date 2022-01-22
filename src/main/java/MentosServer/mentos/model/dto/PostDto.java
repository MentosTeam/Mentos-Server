package MentosServer.mentos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostDto {
	
	private String postId;
	
	private String memberId;
	
	private String postTitle;
	
	private String postContents;
}
