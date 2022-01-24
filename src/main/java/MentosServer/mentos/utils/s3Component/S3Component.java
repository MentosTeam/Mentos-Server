package MentosServer.mentos.utils.s3Component;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "cloud.aws.s3")
@Component
public class S3Component {
    private String bucket; //configuration properties를 통해 프로퍼티의 값을 불러오도록 한다.
}
