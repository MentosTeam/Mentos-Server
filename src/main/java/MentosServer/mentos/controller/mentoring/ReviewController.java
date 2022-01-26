package MentosServer.mentos.controller.mentoring;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.model.dto.PostLoginReq;
import MentosServer.mentos.model.dto.PostLoginRes;
import MentosServer.mentos.model.dto.mentoring.PostReviewReq;
import MentosServer.mentos.model.dto.mentoring.PostReviewRes;
import MentosServer.mentos.service.LoginService;
import MentosServer.mentos.service.mentoring.ReviewService;
import MentosServer.mentos.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static MentosServer.mentos.config.BaseResponseStatus.*;
import static MentosServer.mentos.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/mentoring")
public class ReviewController {

    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final ReviewService reviewService;


    public ReviewController(ReviewService reviewService, JwtService jwtService) {
        this.reviewService = reviewService;
        this.jwtService = jwtService;
    }

    /**
     * 리뷰 API
     * [POST] /mentoring/review
     * @PATH mentoringId
     */
    @ResponseBody
    @PostMapping("/review/{mentoringId}")
    public BaseResponse<PostReviewRes> review(@PathVariable("mentoringId") int mentoringId, @RequestBody PostReviewReq postReviewReq) {
        if(mentoringId != postReviewReq.getMentoringId()){
            return new BaseResponse<>(INVALID_ACCESS);
            //pathvariable의 mentoringId와 requestbody의 mentoringId가 다르면 잘못된 접근 에러리턴
        }

        try {
            int memberIdByJwt = jwtService.getMemberId();

            PostReviewRes postReviewRes = reviewService.review(postReviewReq, memberIdByJwt);
            return new BaseResponse<>(postReviewRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
