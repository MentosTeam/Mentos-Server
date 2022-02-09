package MentosServer.mentos.utils;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponseStatus;
import MentosServer.mentos.utils.s3Component.UploadService;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static MentosServer.mentos.config.BaseResponseStatus.SERVER_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final UploadService s3Service;

    /**
     * Multipart를 통해 전송된 파일을 업로드 하는 메소드
     * MultipartFile에서의 InputStream을 가져와서 실제 AWS S3로 파일을 전송하는 로직은 S3Service에 위임
     * @param file
     * @return
     */
    public String uploadS3Image(MultipartFile file) throws BaseException {
        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다 (%s)", file.getOriginalFilename()));
        }catch (AmazonServiceException e) {
            log.error(e.getErrorMessage());
            throw new BaseException(SERVER_ERROR);
        }
        return s3Service.getFileUrl(fileName);
    }
    //기존 확장자명을 유지한 채, 유니크한 파일의 이름을 생성하는 로직
    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    //파일의 확장자명을 가져오는 로직
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format("잘못된 형식의 파일 (%s) 입니다", fileName));
        }
    }
    // 파일을 삭제하는 메소드
    public void deleteS3Image(String fileUrl){
        String[] fileParsing = fileUrl.split("/");
        s3Service.deleteFile(fileParsing[fileParsing.length-1]);
    }

}
