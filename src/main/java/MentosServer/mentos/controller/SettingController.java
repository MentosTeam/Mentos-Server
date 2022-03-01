package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.config.BaseResponseStatus;
import MentosServer.mentos.model.dto.*;
import MentosServer.mentos.service.SettingService;
import MentosServer.mentos.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static MentosServer.mentos.config.BaseResponseStatus.*;
import static MentosServer.mentos.utils.ValidationRegex.isRegexNickName;

@RestController
@RequestMapping("/settings")
public class SettingController {
    private final SettingService settingService;
    private final JwtService jwtService;

    @Autowired
    public SettingController(SettingService settingService, JwtService jwtService) {
        this.settingService = settingService;
        this.jwtService = jwtService;
    }

    //멘토일 시 설정 클릭
    @GetMapping("/mento")
    public BaseResponse<GetSettingProfileRes> getMentoProfile(){
        try {
            int memberId = jwtService.getMemberId();
            GetSettingProfileRes getMentoProfileRes = settingService.getMentoSettingProfile(memberId);
            return new BaseResponse<>(getMentoProfileRes);
        }catch(BaseException e){
            return new BaseResponse(e.getStatus());
        }
    }
    //멘티일 시 설정 클릭
    @GetMapping("/mentee")
    public BaseResponse<GetSettingProfileRes> getMenteeProfile(){
        try {
            int memberId = jwtService.getMemberId();
            GetSettingProfileRes getMenteeProfileRes = settingService.getMenteeSettingProfile(memberId);
            return new BaseResponse<>(getMenteeProfileRes);
        }catch(BaseException e){
            return new BaseResponse(e.getStatus());
        }

    }


    //학교 전공, 학번 변경
    @PostMapping("/profile/school")
    public BaseResponse changeSchoolInfo(@RequestParam(required = false) String major){
        try{
            int memberId = jwtService.getMemberId();
            settingService.changeSchoolInfo(memberId,major);
            return new BaseResponse(SUCCESS);
        }catch(BaseException e){
            return new BaseResponse(e.getStatus());
        }
    }

    //닉네임 변경
    @PostMapping("/profile")
    public BaseResponse changeNickName(@RequestParam("nickName") String nickName) throws BaseException {

        if(nickName.isEmpty()||nickName==null){ //닉네임을 안적은 경우
            return new BaseResponse(EMPTY_USER_NICKNAME);
        }
        if(!isRegexNickName(nickName)){ //닉네임 형식에 맞지 않는 경우
            return new BaseResponse(INVALID_USER_NICKNAME);
        }
        try{
            int memberId = jwtService.getMemberId();
            settingService.changeNickName(memberId,nickName);
            return new BaseResponse(SUCCESS);
        }catch(BaseException e){
            return new BaseResponse(e.getStatus());
        }
    }
    //프로필 변경
    @PostMapping("/profile/image")
    public BaseResponse changeProfileImage(@ModelAttribute("profileReq")PostProfileImageReq profileImageReq){
        try{
            int memberId = jwtService.getMemberId();
            if(profileImageReq.getRole()==1){ //멘토
                settingService.changeMentoProfileImage(memberId, profileImageReq);
            }
            else if(profileImageReq.getRole()==2) { //멘티
                settingService.changeMenteeProfileImage(memberId,profileImageReq);
            }
            else{
                return new BaseResponse(POST_PROFILE_INVALID_ROLE);
            }
            return new BaseResponse(SUCCESS);
        }catch(BaseException e){
            return new BaseResponse(e.getStatus());
        }
    }

    //멘토 자기소개 변경
    @PostMapping("/profile/intro")
    public BaseResponse changeIntro(@RequestBody PostIntroReq postIntroReq, BindingResult br) throws BaseException {
        //validation 추가
        if(br.hasErrors()){
            String errorName = br.getAllErrors().get(0).getDefaultMessage();
            return new BaseResponse<>(BaseResponseStatus.of(errorName));
        }
        try{
            int memberId = jwtService.getMemberId();
            if(postIntroReq.getRole()==1){ //멘토
                settingService.changeMentoProfileIntro(memberId,postIntroReq);
            }
            else if(postIntroReq.getRole()==2){ //멘티
                settingService.changeMenteeProfileIntro(memberId,postIntroReq);
            }
            else{
                return new BaseResponse(POST_PROFILE_INVALID_ROLE);
            }
            return new BaseResponse(SUCCESS);
        }catch(BaseException e){
            return new BaseResponse(e.getStatus());
        }
    }

    //성별 공개 여부 변경
    @PostMapping("profile/gender")
    public BaseResponse changeGenderFlag() throws BaseException {
        try{
            int memberId = jwtService.getMemberId();
            settingService.changeGender(memberId);
            return new BaseResponse(SUCCESS);
        }catch (BaseException e){
            return new BaseResponse(e.getStatus());
        }
    }

    //푸시 알림 on/off
    @PostMapping("profile/notification")
    public BaseResponse changeNotification() throws BaseException {
        try{
            int memberId = jwtService.getMemberId();
            settingService.changeNotification(memberId);
            return new BaseResponse(SUCCESS);
        }catch(BaseException e){
            return new BaseResponse(e.getStatus());
        }
    }

}
