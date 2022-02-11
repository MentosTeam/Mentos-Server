package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.model.dto.GetMentoringStatus;
import MentosServer.mentos.model.dto.MentoringStatusRes;
import MentosServer.mentos.model.dto.PostLoginReq;
import MentosServer.mentos.model.dto.PostLoginRes;
import MentosServer.mentos.service.LoginService;
import MentosServer.mentos.service.MentoringStatusService;
import MentosServer.mentos.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static MentosServer.mentos.config.BaseResponseStatus.*;
import static MentosServer.mentos.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/members")
public class MentoringStatusController {




    private final JwtService jwtService;
    private final MentoringStatusService mentoringStatusService;

    @Autowired
    public MentoringStatusController(JwtService jwtService, MentoringStatusService mentoringStatusService) {
        this.jwtService = jwtService;
        this.mentoringStatusService = mentoringStatusService;
    }



    /**
     * 메토링 API
     * [GET] /members/mentoring/{memberId}/{profile}
     */
    @ResponseBody
    @GetMapping("/mentoring/{profile}")
    public BaseResponse<GetMentoringStatus> mentoringStatus(@PathVariable("profile") String profile) {

        try {
            int memberId = jwtService.getMemberId();

            if(Objects.equals(profile, "mentor")){

                GetMentoringStatus getStatusRes = mentoringStatusService.getMentoringList(memberId, "mentor");
                return new BaseResponse<>(getStatusRes);
            }
            else {
                GetMentoringStatus getStatusRes = mentoringStatusService.getMentoringList(memberId, "mentee");
                return new BaseResponse<>(getStatusRes);
            }


        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}

