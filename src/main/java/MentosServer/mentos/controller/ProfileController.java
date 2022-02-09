package MentosServer.mentos.controller;


import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.model.dto.PostProfileReq;
import MentosServer.mentos.model.dto.PostProfileRes;
import MentosServer.mentos.service.ProfileService;
import MentosServer.mentos.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static MentosServer.mentos.config.BaseResponseStatus.*;
import static MentosServer.mentos.utils.ValidationRegex.isRegexImgUrl;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final JwtService jwtService;

    @Autowired
    public ProfileController(ProfileService profileService, JwtService jwtService){
        this.profileService = profileService;
        this.jwtService = jwtService;
    }

    /**
     * 멘토/멘티 프로필 설정 API
     * [POST] /setProfile
     * 1. 회원가입 시 처음 멘토 or 멘티 프로필 등록
     * 2. 기존 프로필 외에 다른 프로필 등록 (기존에 멘토 프로필이 존재-> 새로 만들어지는 프로필은 멘티 프로필)
     * 2의 경우, role이 1이든 2이든 상관 없음.
     */
    @ResponseBody
    @PostMapping("/setProfile")
    public BaseResponse<PostProfileRes> createProfile(@ModelAttribute PostProfileReq postProfileReq){
        if(postProfileReq.getRole() != 1 && postProfileReq.getRole() != 2){ //역할 설정 여부 확인
            return new BaseResponse<>(POST_PROFILE_INVALID_ROLE);
        }

        if(postProfileReq.getMajorFirst() == 0){ //첫 번째 멘토쓰(major) 선택 여부 확인
            return new BaseResponse<>(POST_PROFILE_EMPTY_MAJORFIRST);
        }

        if(postProfileReq.getIntroduction().equals("")){ //자기소개 입력 확인
            return new BaseResponse<>(POST_PROFILE_EMPTY_INTRODUCTION);
        }

        if (postProfileReq.getIntroduction().length() < 10){ //자기소개 10글자 이상 입력 확인
            return new BaseResponse<>(POST_PROFILE_SHORT_INTRODUCTION);
        }

        try{
            PostProfileRes postProfileRes = profileService.createProfile(postProfileReq, jwtService.getMemberId());
            return new BaseResponse<>(postProfileRes);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
