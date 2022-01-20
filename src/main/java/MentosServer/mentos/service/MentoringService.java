package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.model.dto.PostMentoringReq;
import MentosServer.mentos.model.dto.PostMentoringRes;
import MentosServer.mentos.repository.MentoringRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static MentosServer.mentos.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class MentoringService {
    private final MentoringRepository mentoringRepository;

    @Autowired
    public MentoringService(MentoringRepository mentoringRepository) {
        this.mentoringRepository = mentoringRepository;
    }

    //멘토링 등록
    public PostMentoringRes createMentoring(PostMentoringReq postMentoringReq) throws BaseException {
        try{
            int mentoringId = mentoringRepository.createMentoring(postMentoringReq);
            PostMentoringRes postMentoringRes = new PostMentoringRes(mentoringId, postMentoringReq.getMentoId(), postMentoringReq.getMentiId());

            return postMentoringRes;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}