package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.config.BaseResponseStatus;
import MentosServer.mentos.model.dto.PatchStopMentoringRes;
import MentosServer.mentos.model.dto.PostAcceptMentoringRes;
import MentosServer.mentos.model.dto.PostMentoringReq;
import MentosServer.mentos.model.dto.PostMentoringRes;
import MentosServer.mentos.service.MentoringService;
import MentosServer.mentos.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static MentosServer.mentos.config.BaseResponseStatus.POST_MENTORING_SAME_MENTOMENTI;

@RestController
@RequestMapping("/mentoring")
public class MentoringController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final MentoringService mentoringService;
    private final JwtService jwtService;

    @Autowired
    public MentoringController(MentoringService mentoringService, JwtService jwtService) {
        this.mentoringService = mentoringService;
        this.jwtService = jwtService;
    }

    /**
     * 멘토링 등록 API
     * @param postMentoringReq
     * @return PostMentoringRes => 멘토링 등록 ID, 멘토 ID, 멘티 ID
     */
    @ResponseBody
    @PostMapping("/registration")
    public BaseResponse<PostMentoringRes> createMentoring(@Valid @RequestBody PostMentoringReq postMentoringReq, BindingResult br){
        if(br.hasErrors()){
            String errorName = br.getAllErrors().get(0).getDefaultMessage();
            return new BaseResponse<>(BaseResponseStatus.of(errorName));
        }

        try{
            int memberIdByJwt = jwtService.getMemberId();
            if(memberIdByJwt == postMentoringReq.getMentoId()){ //멘토와 멘티 같은 유저인지 확인
                return new BaseResponse<>(POST_MENTORING_SAME_MENTOMENTI);
            }

            postMentoringReq.setMentiId(memberIdByJwt);
            PostMentoringRes postMentoringRes = mentoringService.createMentoring(postMentoringReq);
            return new BaseResponse<>(postMentoringRes);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 멘토링 요청 수락/거절 API
     * @param mentoringId
     * @return PostAcceptMentoringRes
     */
    @ResponseBody
    @PostMapping("/acceptance")
    public BaseResponse<PostAcceptMentoringRes> acceptMentoring(@RequestParam("mentoringId") int mentoringId, @RequestParam("accept") Boolean acceptance){
        try{
            int mentoIdByJwt = jwtService.getMemberId();
            PostAcceptMentoringRes postAcceptMentoringRes = mentoringService.acceptMentoring(mentoringId, mentoIdByJwt, acceptance);

            return new BaseResponse<>(postAcceptMentoringRes);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 멘토링 강제 종료 API
     * @param mentoringId
     * @return
     */
    @ResponseBody
    @PatchMapping("/stop")
    public BaseResponse<PatchStopMentoringRes> stopMentoring(@RequestParam("mentoringId") int mentoringId){
        try{
            int mentiByJwt = jwtService.getMemberId();
            PatchStopMentoringRes patchStopMentoringRes = mentoringService.stopMentoring(mentoringId, mentiByJwt);

            return new BaseResponse<>(patchStopMentoringRes);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
