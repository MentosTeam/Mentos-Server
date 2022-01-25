package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.config.BaseResponseStatus;
import MentosServer.mentos.model.dto.SignUpReq;
import MentosServer.mentos.model.dto.SignUpRes;
import MentosServer.mentos.service.SignUpService;
import MentosServer.mentos.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static MentosServer.mentos.config.BaseResponseStatus.*;
import static MentosServer.mentos.utils.ValidationRegex.isRegexNickName;

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
    @PostMapping("/sign-up")
    public BaseResponse<SignUpRes> createMember(@Valid @RequestBody SignUpReq signUpReq, BindingResult br){
        //validation 추가
        if(br.hasErrors()){
            String errorName = br.getAllErrors().get(0).getDefaultMessage();
            return new BaseResponse<>(BaseResponseStatus.of(errorName));
        }
        //service 호출
        try{
            SignUpRes signUpRes = signUpService.createMember(signUpReq);
            return new BaseResponse<>(signUpRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/nickNameChk")
    public BaseResponse checkNickName(@RequestParam String nickName){
        if(nickName==null || nickName.equals("")){
            return new BaseResponse<>(EMPTY_USER_NICKNAME);
        }
        if(!isRegexNickName(nickName)) {
            return new BaseResponse<>(INVALID_USER_NICKNAME);
        }
        //service 호출
        try{
            if(signUpService.checkNickName(nickName)==0){
                return new BaseResponse<>(VALID_USER_NICKNAME);
            }
            else{
                return new BaseResponse<>(DUPLICATED_NICKNAME);
            }
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
