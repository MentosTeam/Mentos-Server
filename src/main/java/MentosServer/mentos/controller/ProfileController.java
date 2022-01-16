package MentosServer.mentos.controller;


import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.model.dto.PostProfileReq;
import MentosServer.mentos.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static MentosServer.mentos.config.BaseResponseStatus.*;
import static MentosServer.mentos.utils.ValidationRegex.isRegexImgUrl;

@RestController
@RequestMapping("")
public class ProfileController {
    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService){
        this.profileService = profileService;
    }

    /**
     * 멘토/멘티 프로필 설정 API
     * [POST] /setProfile
     * 1. 회원가입 시 처음 멘토 or 멘티 프로필 등록
     * 2. 기존 프로필 외에 다른 프로필 등록 (기존에 멘토 프로필이 존재-> 새로 만들어지는 프로필은 멘티 프로필)
     * 2의 경우, role이 1이든 2이든 상관 없음.
     */
    @ResponseBody
    @PostMapping("/members/setProfile")
    public BaseResponse<String> createProfile(@RequestBody PostProfileReq postProfileReq){
        if(postProfileReq.getRole() != 1 && postProfileReq.getRole() != 2){ //역할 설정 여부 확인
            return new BaseResponse<>(POST_PROFILE_INVALID_ROLE);
        }
        if(postProfileReq.getMemberId() == 0){ //memberId 입력 확인
            return new BaseResponse<>(POST_PROFILE_EMPTY_MEMBERID);
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

        if (postProfileReq.getImageUrl() != null && (!postProfileReq.getImageUrl().equals(""))) {
            if (!isRegexImgUrl(postProfileReq.getImageUrl())) {//이미지 확장자 확인
                return new BaseResponse<>(POST_PROFILE_INVALID_IMAGEURL);
            }
        }

        try{
            profileService.createProfile(postProfileReq);
            return new BaseResponse<>("프로필이 성공적으로 등록되었습니다.");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
