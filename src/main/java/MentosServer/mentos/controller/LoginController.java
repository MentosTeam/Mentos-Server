package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.model.dto.PostLoginReq;
import MentosServer.mentos.model.dto.PostLoginRes;
import MentosServer.mentos.service.LoginService;
import MentosServer.mentos.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static MentosServer.mentos.config.BaseResponseStatus.POST_USERS_INVALID_EMAIL;
import static MentosServer.mentos.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/members")
public class LoginController {



    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final LoginService loginService;


    public LoginController(LoginService loginService, JwtService jwtService) {
        this.loginService = loginService;
        this.jwtService = jwtService;
    }



    /**
     * 로그인 API
     * [POST] /members/logIn
     */
    @ResponseBody
    @PostMapping("/log-in")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq) {
        if (!isRegexEmail(postLoginReq.getMemberEmail())) {
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        try {
            PostLoginRes postLoginRes = loginService.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
