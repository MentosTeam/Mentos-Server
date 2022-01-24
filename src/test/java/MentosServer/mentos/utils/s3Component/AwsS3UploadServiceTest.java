package MentosServer.mentos.utils.s3Component;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AwsS3UploadServiceTest {

    @Autowired
    private AwsS3UploadService uploadService;


    // url 파싱 잘되는지 확인하는 메소드
    @Test
    public void urlParsing(){
        //given
        String fileUrl = "https://beads-aws-s3.s3.ap-northeast-2.amazonaws.com/dbaa4a2a-81c9-4ee0-b886-b253f76989f5.png";
        //when
        String fileName = fileUrl.substring(fileUrl.lastIndexOf('/')+1);
        //then
        assertEquals("dbaa4a2a-81c9-4ee0-b886-b253f76989f5.png",fileName);
    }

    // 이미지 삭제 확인 메소드
    @Test
    void deleteFile() {
        //given
        String fileUrl = "https://beads-aws-s3.s3.ap-northeast-2.amazonaws.com/dbaa4a2a-81c9-4ee0-b886-b253f76989f5.png";
        String fileName = fileUrl.substring(fileUrl.lastIndexOf('/')+1);
        //when
        uploadService.deleteFile(fileName);
    }

}