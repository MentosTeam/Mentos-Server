package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.model.dto.GetMenteeProfileRes;
import MentosServer.mentos.service.MenteeProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class MenteeProfileController {
    private final MenteeProfileService menteeProfileService;

    @Autowired
    public MenteeProfileController(MenteeProfileService menteeProfileService){
        this.menteeProfileService = menteeProfileService;
    }

    @GetMapping("/mentee")
    public BaseResponse<GetMenteeProfileRes> getMenteeProfile(@RequestParam int memberId){
        try{
            GetMenteeProfileRes getMenteeProfileRes = menteeProfileService.getMenteeProfile(memberId);
            return new BaseResponse<>(getMenteeProfileRes);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
