package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.model.domain.Mentee;
import MentosServer.mentos.model.domain.Mento;
import MentosServer.mentos.model.dto.*;
import MentosServer.mentos.repository.SettingRepository;
import MentosServer.mentos.utils.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static MentosServer.mentos.config.BaseResponseStatus.*;

@Slf4j
@Service
public class SettingService {
    private final SignUpService signUpService;
    private final FileUploadService fileUploadService;
    private final SettingRepository settingRepository;

    public SettingService(SignUpService signUpService, FileUploadService fileUploadService, SettingRepository settingRepository) {
        this.signUpService = signUpService;
        this.fileUploadService = fileUploadService;
        this.settingRepository = settingRepository;
    }

    //전공 및 학번 변경
    public void changeSchoolInfo(int memberId, String major) throws BaseException {
        try {
            settingRepository.changeSchoolInfo(memberId,major);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //닉네임 변경
    public void changeNickName(int memberId,String nickName) throws BaseException {
        //닉네임 중복확인 후 변경
        if (signUpService.checkNickName(nickName) == 1) {
            throw new BaseException(DUPLICATED_NICKNAME);
        }
        try {
            settingRepository.changeNickName(memberId,nickName);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /*
   멘토 프로필 변경
     */
    public void changeMentoProfileImage(int memberId, PostProfileImageReq profileImageReq) throws BaseException {
        try {
            Optional<String> imageUrl = settingRepository.getMentoProfileImageUrl(memberId);
            //Image File 없으면 -> 이미지 삭제 여부 확인
            if (profileImageReq.getImageFile() == null || profileImageReq.getImageFile().isEmpty()) {
                if (profileImageReq.getImageUrl() == null || profileImageReq.getImageUrl().isEmpty()) { //이미지 삭제를 했다면?
                    //기존 이미지 있었는지 확인하고 삭제
                    if (imageUrl.isPresent()) { //
                        fileUploadService.deleteS3Image(imageUrl.get());
                        settingRepository.deleteMentoProfileImage(memberId);
                    }
                }

            }
            //요청 이미지 파일 있다면? -> 교체
            else {
                if (imageUrl.isPresent()) {
                    fileUploadService.deleteS3Image(imageUrl.get());
                }
                //새로운 이미지 업로드
                String newImageUrl = fileUploadService.uploadS3Image(profileImageReq.getImageFile());
                settingRepository.updateMentoProfileImage(memberId, newImageUrl);
            }
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /*
    멘티 프로필 변경
     */
    public void changeMenteeProfileImage(int memberId,PostProfileImageReq profileImageReq) throws BaseException {
        try {
            Optional<String> imageUrl = settingRepository.getMenteeProfileImageUrl(memberId);

            //Image File 없으면 -> 이미지 삭제 여부 확인
            if (profileImageReq.getImageFile() == null) {
                if (profileImageReq.getImageUrl() == null || profileImageReq.getImageUrl().isEmpty()) { //이미지 삭제를 했다면?
                    //기존 이미지 있었는지 확인하고 삭제
                    if (imageUrl.isPresent()) { //
                        fileUploadService.deleteS3Image(imageUrl.get());
                        settingRepository.deleteMenteeProfileImage(memberId);
                    }
                }

            }
            //요청 이미지 파일 있다면? -> 교체
            else {
                if (imageUrl.isPresent()) {
                    fileUploadService.deleteS3Image(imageUrl.get()); //S3 이미지 삭제
                }
                //새로운 이미지 업로드
                String newImageUrl = fileUploadService.uploadS3Image(profileImageReq.getImageFile());
                settingRepository.updateMenteeProfileImage(memberId, newImageUrl);
            }
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /*
    멘티 자기소개 변경
     */
    public void changeMenteeProfileIntro(int memberId, PostIntroReq postIntroReq) throws BaseException {
        try{
            settingRepository.changeMenteeProfileIntro(memberId,postIntroReq);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    /*
    멘토 자기소개 변경
     */
    public void changeMentoProfileIntro(int memberId, PostIntroReq postIntroReq) throws BaseException {
        try{
            settingRepository.changeMentoProfileIntro(memberId,postIntroReq);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }


    //설정 시 필요한 멘토 정보 조회
    public GetSettingProfileRes getMentoSettingProfile(int memberId) throws BaseException {
        Mento mento = settingRepository.getMentoProfile(memberId);
        if(mento==null){
            throw new BaseException(EMPTY_MENTO_PROFILE);
        }
        try{
            GetSettingProfileRes getMentoProfileRes = new GetSettingProfileRes(mento.getMember().getMemberNickName(),
                    mento.getMember().getMemberStudentId(),mento.getMember().getMemberMajor(),
                    mento.getMentoMajorFirst(), mento.getMentoMajorSecond(),
                    mento.getMentoImage(), mento.getMentoIntro());

            return getMentoProfileRes;
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    //설정 시 필요한 멘티 정보 조회
    public GetSettingProfileRes getMenteeSettingProfile(int memberId) throws BaseException {
        Mentee mentee = settingRepository.getMenteeProfile(memberId);
        if(mentee==null){
            throw new BaseException(EMPTY_MENTEE_PROFILE);
        }
        try{
            GetSettingProfileRes getMenteeProfileRes = new GetSettingProfileRes(mentee.getMember().getMemberNickName(),
                    mentee.getMember().getMemberStudentId(),mentee.getMember().getMemberMajor(),
                    mentee.getMenteeMajorFirst(), mentee.getMenteeMajorSecond(),
                    mentee.getMenteeImage(), mentee.getMenteeIntro());

            return getMenteeProfileRes;
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void changeGender(int memberId) throws BaseException {
        try{
            settingRepository.changeGender(memberId);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void changeNotification(int memberId) throws BaseException {
        try{
            settingRepository.changeNotification(memberId);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
