package MentosServer.mentos.model.domain;

import lombok.Data;
import lombok.NonNull;

import java.util.Map;

@Data
public class PostImage {
    @NonNull
    private int postId;
    @NonNull
    private String imageUrl;
}
