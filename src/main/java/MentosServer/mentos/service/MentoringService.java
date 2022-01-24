package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.model.dto.PatchStopMentoringRes;
import MentosServer.mentos.model.dto.PostAcceptMentoringRes;
import MentosServer.mentos.model.dto.PostMentoringReq;
import MentosServer.mentos.model.dto.PostMentoringRes;
import MentosServer.mentos.repository.MentoringRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static MentosServer.mentos.config.BaseResponseStatus.*;

@Service
public class MentoringService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final MentoringRepository mentoringRepository;

    @Autowired
    public MentoringService(MentoringRepository mentoringRepository) {
        this.mentoringRepository = mentoringRepository;
    }

    //멘토링 등록
    public PostMentoringRes createMentoring(PostMentoringReq postMentoringReq) throws BaseException {
        if(mentoringRepository.checkMentoring(postMentoringReq) == 1){ // 멘토링 중복 신청 확인
            throw new BaseException(POST_MENTORING_DUPLICATED_MENTORING);
        }

        try{
            int mentoringId = mentoringRepository.createMentoring(postMentoringReq);

            PostMentoringRes postMentoringRes = new PostMentoringRes(mentoringId, postMentoringReq.getMentoId(), postMentoringReq.getMentiId());
            logger.info("멘토링 신청");

            return postMentoringRes;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //멘토링 수락/거절
    public PostAcceptMentoringRes acceptMentoring(int mentoringId, int mentoId, Boolean acceptance) throws BaseException{
        if(mentoringRepository.checkMentoringByMento(mentoringId, mentoId) == 0){ //멘토링 요청 존재 확인
            throw new BaseException(POST_INVALID_MENTORING);
        }

        try{
            PostAcceptMentoringRes postAcceptMentoringRes = new PostAcceptMentoringRes(mentoringId, mentoId, "");
            if(acceptance){
                if(mentoringRepository.acceptMentoring(mentoringId) == 0){
                    throw new BaseException(FAILED_TO_ACCEPTMENTORING);
                }
                postAcceptMentoringRes.setStatus("성공적으로 멘토링 요청을 수락했습니다.");
            }
            else {
                if(mentoringRepository.rejectMentoring(mentoringId) == 0){
                    throw new BaseException(FAILED_TO_REJECTMENTORING);
                }
                postAcceptMentoringRes.setStatus("성공적으로 멘토링 요청을 거절했습니다.");
            }

            return postAcceptMentoringRes;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //멘토링 강제 종료
    public PatchStopMentoringRes stopMentoring(int mentoringId, int mentiId) throws BaseException{
        if(mentoringRepository.checkMentoringByMenti(mentoringId, mentiId) == 0){
            throw new BaseException(PATCH_INVALID_MENTORING);
        }

        try{
            if(mentoringRepository.stopMentoring(mentoringId) == 0){
                throw new BaseException(FAILED_TO_STOPMENTORING);
            }

            PatchStopMentoringRes patchStopMentoringRes = new PatchStopMentoringRes(mentoringId, mentiId, "멘토링이 강제 종료 되었습니다.");
            return patchStopMentoringRes;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}