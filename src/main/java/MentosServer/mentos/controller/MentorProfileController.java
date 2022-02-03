package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.model.dto.GetMentorProfileRes;
import MentosServer.mentos.service.MentorProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class MentorProfileController {
    private final MentorProfileService mentorProfileService;

    @Autowired
    public MentorProfileController(MentorProfileService mentorProfileService){
        this.mentorProfileService = mentorProfileService;
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
}
