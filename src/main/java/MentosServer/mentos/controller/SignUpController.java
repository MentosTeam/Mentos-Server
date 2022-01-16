package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.model.dto.NickNameChkReq;
import MentosServer.mentos.model.dto.NickNameChkRes;
import MentosServer.mentos.model.dto.SignUpReq;
import MentosServer.mentos.model.dto.SignUpRes;
import MentosServer.mentos.service.SignUpService;
import MentosServer.mentos.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class SignUpController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final SignUpService signUpService;
    private final JwtService jwtService;

    @Autowired
    public SignUpController(SignUpService signUpService, JwtService jwtService) {
        this.signUpService = signUpService;
        this.jwtService = jwtService;
    }

    /**
     * 회원가입 API
     * @param signUpReq
     * @return signUpRes : memberId를 리턴
     */
    @ResponseBody
    @PostMapping("/sign-up")
    public BaseResponse<SignUpRes> createMember(@RequestBody SignUpReq signUpReq){
        //validation 추가 - @Validated 사용
        //service 호출
        try{
            SignUpRes signUpRes = signUpService.createMember(signUpReq);
            return new BaseResponse<>(signUpRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
    @ResponseBody
    @PostMapping("/nickNameChk")
    public BaseResponse<NickNameChkRes> checkNickName(@RequestBody NickNameChkReq nickChkReq){
        //validation - 닉네임 형식
        //service 호출
        try{
            NickNameChkRes nickChkRes = signUpService.checkNickName(nickChkReq);
            return new BaseResponse<>(nickChkRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
