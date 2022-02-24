package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.model.dto.GetNoticeRes;
import MentosServer.mentos.model.dto.GetNotificationRes;
import MentosServer.mentos.model.dto.PostLoginReq;
import MentosServer.mentos.model.dto.PostLoginRes;
import MentosServer.mentos.service.LoginService;
import MentosServer.mentos.service.NoticeService;
import MentosServer.mentos.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static MentosServer.mentos.config.BaseResponseStatus.POST_USERS_INVALID_EMAIL;
import static MentosServer.mentos.utils.ValidationRegex.isRegexEmail;

@RestController
public class NoticeController {



    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final NoticeService noticeService;


    public NoticeController(NoticeService noticeService, JwtService jwtService) {
        this.noticeService = noticeService;
        this.jwtService = jwtService;
    }

    /**
     * 공지조회 API
     * [POST] /notice
     */
    @GetMapping("/notice")
    public BaseResponse<List<GetNoticeRes>> noticeList() {
        try {
            List<GetNoticeRes> getNoticeRes = noticeService.noticeList();
            return new BaseResponse<>(getNoticeRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/notification")
    public BaseResponse<List<GetNotificationRes>> getNotificationList(@RequestParam int statusFlag) throws BaseException {
        int memberId = jwtService.getMemberId();
        try {
            List<GetNotificationRes> getNotificationRes = noticeService.notificationList(memberId,statusFlag);
            return new BaseResponse<>(getNotificationRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
