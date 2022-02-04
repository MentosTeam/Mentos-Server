package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.secret.Secret;
import MentosServer.mentos.model.domain.Mentoring;
import MentosServer.mentos.model.dto.PostReviewReq;
import MentosServer.mentos.model.dto.PostReviewRes;
import MentosServer.mentos.repository.LoginRepository;
import MentosServer.mentos.repository.MentoringRepository;
import MentosServer.mentos.repository.ReviewRepository;
import MentosServer.mentos.utils.AES128;
import MentosServer.mentos.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static MentosServer.mentos.config.BaseResponseStatus.*;
import static MentosServer.mentos.config.BaseResponseStatus.FAILED_TO_LOGIN;

@Service
public class ReviewService {

    private final JwtService jwtService;
    private final ReviewRepository reviewRepository;
    private final MentoringRepository mentoringRepository;



    @Autowired
    public ReviewService(ReviewRepository reviewRepository, MentoringRepository mentoringRepository, JwtService jwtService) {
        this.reviewRepository = reviewRepository;
        this.jwtService = jwtService;
        this.mentoringRepository = mentoringRepository;
    }

    // 리뷰
    public PostReviewRes review(PostReviewReq postReviewReq, int memberId) throws BaseException {
        Mentoring mentoring = mentoringRepository.getMentoring(postReviewReq.getMentoringId()); //멘토링 정보 생성

        if(mentoring.getMentoringMentiId()!=memberId){
            throw new BaseException(INVALID_USER_JWT);
            //멘토링의 멘티와 리뷰의 멘티가 같지 않다면 에러 출력
        }

        if(mentoringRepository.checkMentoringByMenti(mentoring.getMentoringId(), mentoring.getMentoringMentiId(), 1) == 0){
            throw new BaseException(PATCH_INVALID_MENTORING);
            //멘토링의 유효여부 확인
        }

        if(reviewRepository.checkReview(mentoring.getMentoringId())==1){
            throw new BaseException(POST_REVIEW_EXISTS);
            //리뷰가 이미 등록되어 있다면 에러 출력
        }

        try {
            int reviewId = reviewRepository.createReview(postReviewReq);
            return new PostReviewRes(reviewId, postReviewReq.getReviewScore());

        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
