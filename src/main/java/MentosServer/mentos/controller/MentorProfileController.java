package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.model.dto.GetMentorProfileRes;
import MentosServer.mentos.model.dto.PostWithImageDto;
import MentosServer.mentos.service.MentorProfileService;
import MentosServer.mentos.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class MentorProfileController {
    private final MentorProfileService mentorProfileService;
    private final JwtService jwtService;

    @Autowired
    public MentorProfileController(MentorProfileService mentorProfileService, JwtService jwtService){
        this.mentorProfileService = mentorProfileService;
        this.jwtService = jwtService;
    }

    @GetMapping("/mentor")
    public BaseResponse<GetMentorProfileRes> getMentorProfile(@RequestParam int memberId){
        try{
            GetMentorProfileRes getMentorProfileRes = mentorProfileService.getMentorProfile(memberId);
            return new BaseResponse<>(getMentorProfileRes);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 내 멘토쓰 찾기 글 조회 API
     * @return GetMyPostsRes
     */
    @GetMapping("/myposts")
    public BaseResponse<List<PostWithImageDto>> getMyposts(){
        try {
            List<PostWithImageDto> postArr = mentorProfileService.getMyPosts(jwtService.getMemberId());
            return new BaseResponse<>(postArr);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
