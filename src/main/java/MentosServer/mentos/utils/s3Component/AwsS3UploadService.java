package MentosServer.mentos.utils.s3Component;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@RequiredArgsConstructor
@Component
public class AwsS3UploadService implements UploadService{
    private final AmazonS3Client amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket; //S3 버킷 이름

    @Override
    public void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName) {
        amazonS3.putObject(new PutObjectRequest(bucket,fileName,inputStream,objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

    }

    @Override
    public String getFileUrl(String fileName) {
        return amazonS3.getUrl(bucket,fileName).toString();
    }

    @Override
    public void deleteFile(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));

    }
}
