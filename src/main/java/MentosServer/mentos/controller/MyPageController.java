package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.model.dto.GetMyPageRes;
import MentosServer.mentos.service.MyPageService;
import MentosServer.mentos.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class MyPageController {
    private final MyPageService myPageService;
    private final JwtService jwtService;

    @Autowired
    public MyPageController(MyPageService myPageService, JwtService jwtService){
        this.myPageService = myPageService;
        this.jwtService = jwtService;
    }

    @GetMapping("/mypage")
    public BaseResponse<GetMyPageRes> getMyPage(){
        try{
            int memberId = jwtService.getMemberId();
            GetMyPageRes getMyPageRes = myPageService.getMyPage(memberId);
            return new BaseResponse<>(getMyPageRes);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
