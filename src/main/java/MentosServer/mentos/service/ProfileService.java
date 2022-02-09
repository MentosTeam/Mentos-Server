package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.model.dto.PostProfileReq;
import MentosServer.mentos.model.dto.PostProfileRes;
import MentosServer.mentos.repository.ProfileRepository;
import MentosServer.mentos.utils.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static MentosServer.mentos.config.BaseResponseStatus.*;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final FileUploadService fileUploadService;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, FileUploadService fileUploadService){
        this.profileRepository = profileRepository;
        this.fileUploadService = fileUploadService;
    }

    //프로필 등록
    public PostProfileRes createProfile(PostProfileReq postProfileReq, int memberId) throws BaseException {
        int exitMento, exitMenti;

        try{
            exitMento = profileRepository.checkMentoProfile(memberId); //멘토 프로필 존재 여부
            exitMenti = profileRepository.checkMentiProfile(memberId); //멘티 프로필 존재 여부
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }

        if (exitMento == 1 && exitMenti == 1) { //멘토, 멘티 프로필이 모두 존재하는 경우
            throw new BaseException(POST_DUPLICATED_PROFILE);
        }

        PostProfileRes postProfileRes = new PostProfileRes(memberId, "");
        try{
            String imageUrl = null;

            if(postProfileReq.getImageFile() != null && !postProfileReq.getImageFile().isEmpty()) {
                imageUrl = fileUploadService.uploadS3Image(postProfileReq.getImageFile());
            }

            if ((postProfileReq.getRole() == 1 && exitMento == 0) || ((exitMento != exitMenti) && exitMento == 0)) {//멘토 프로필 생성

                profileRepository.createMentoProfile(postProfileReq, imageUrl, memberId);

                postProfileRes.setProfile("멘토 프로필 생성");
            }
            else if ((postProfileReq.getRole() == 2 && exitMenti == 0) || ((exitMento != exitMenti) && exitMento == 1)) {//멘티 프로필 생성

                profileRepository.createMentiProfile(postProfileReq, imageUrl, memberId);

                postProfileRes.setProfile("멘티 프로필 생성");
            }
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }

        return postProfileRes;
    }
}
