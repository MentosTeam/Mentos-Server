package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.model.domain.Mentee;
import MentosServer.mentos.model.domain.Mento;
import MentosServer.mentos.model.domain.PostImage;
import MentosServer.mentos.model.dto.*;
import MentosServer.mentos.repository.SettingRepository;
import MentosServer.mentos.utils.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static MentosServer.mentos.config.BaseResponseStatus.DATABASE_ERROR;
import static MentosServer.mentos.config.BaseResponseStatus.DUPLICATED_NICKNAME;

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
    public void changeSchoolInfo(int memberId, String major, int studentId) throws BaseException {
        try {
            settingRepository.changeSchoolInfo(memberId,major,studentId);
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
            settingRepository.changeMenteeProfileIntro(memberId,postIntroReq.getIntro());
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    /*
    멘토 자기소개 변경
     */
    public void changeMentoProfileIntro(int memberId, PostIntroReq postIntroReq) throws BaseException {
        try{
            settingRepository.changeMentoProfileIntro(memberId,postIntroReq.getIntro());
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /*
    멘토스 계열 변경
     */
    public void changeMentoMentosMajor(int memberId, PostMentosMajorReq mentosMajorReq) throws BaseException {
        try{
            settingRepository.changeMentoMentosMajor(memberId,mentosMajorReq);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void changeMenteeMentosMajor(int memberId, PostMentosMajorReq mentosMajorReq) throws BaseException {
        try{
            settingRepository.changeMenteeMentosMajor(memberId,mentosMajorReq);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //설정 시 필요한 멘토 정보 조회
    public GetMentoProfileRes getMentoSettingProfile(int memberId) throws BaseException {
        try{
            Mento mento = settingRepository.getMentoProfile(memberId);
            GetMentoProfileRes getMentoProfileRes = new GetMentoProfileRes(mento.getMember().getMemberNickName(),
                    mento.getMember().getMemberStudentId(),mento.getMember().getMemberMajor(),
                    mento.getMentoMajorFirst(), mento.getMentoMajorSecond(),
                    mento.getMentoImage(), mento.getMentoIntro());

            return getMentoProfileRes;
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    //설정 시 필요한 멘티 정보 조회
    public GetMenteeProfileRes getMenteeSettingProfile(int memberId) throws BaseException {
        try{
            Mentee mentee = settingRepository.getMenteeProfile(memberId);
            GetMenteeProfileRes getMenteeProfileRes = new GetMenteeProfileRes(mentee.getMember().getMemberNickName(),
                    mentee.getMember().getMemberStudentId(),mentee.getMember().getMemberMajor(),
                    mentee.getMenteeMajorFirst(), mentee.getMenteeMajorSecond(),
                    mentee.getMenteeImage(), mentee.getMenteeIntro());

            return getMenteeProfileRes;
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
