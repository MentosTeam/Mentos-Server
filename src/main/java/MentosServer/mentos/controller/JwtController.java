package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.model.dto.TokenRes;
import MentosServer.mentos.utils.JwtService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static MentosServer.mentos.config.BaseResponseStatus.FAILED_TO_LOGOUT;
import static MentosServer.mentos.config.BaseResponseStatus.SUCCESS_LOGOUT;

@RestController
public class JwtController {
    private final JwtService jwtService;

    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /*
    새로운 액세스 토큰 혹은 리프레시 토큰이 필요한 경우
     */
    @PostMapping("/accessToken")
    public BaseResponse<TokenRes> createRefreshToken() throws BaseException {
        return new BaseResponse<>(jwtService.checkRefreshJwt());
    }
    /*
    로그아웃
     */
    @GetMapping("/log-out")
    public BaseResponse logOut() throws BaseException {
        //리프레시 토큰 버리고
        try {
            jwtService.logOut();
            //반환
            return new BaseResponse<>(SUCCESS_LOGOUT);
        }catch(Exception e){
            return new BaseResponse<>(FAILED_TO_LOGOUT);
        }
    }
}
