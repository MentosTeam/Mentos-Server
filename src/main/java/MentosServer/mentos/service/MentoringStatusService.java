package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;

import MentosServer.mentos.model.dto.EndMentoringRes;

import MentosServer.mentos.model.dto.GetMentoringStatus;
import MentosServer.mentos.model.dto.NowMentoringRes;
import MentosServer.mentos.model.dto.MentoringStatusRes;
import MentosServer.mentos.repository.MentoringStatusRepository;
import MentosServer.mentos.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static MentosServer.mentos.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class MentoringStatusService {

    private final JwtService jwtService;
    private final MentoringStatusRepository mentoringStatusRepository;



    @Autowired
    public MentoringStatusService(MentoringStatusRepository mentoringStatusRepository, JwtService jwtService) {
        this.mentoringStatusRepository = mentoringStatusRepository;
        this.jwtService = jwtService;
    }

    public GetMentoringStatus getMentoringList(int memberId, String profile) throws BaseException {
        try {
            if(Objects.equals(profile, "mentor")){
            List<NowMentoringRes> getMentoringNowList = mentoringStatusRepository.getMentorMentoringNowList(memberId);
            List<EndMentoringRes> getMentoringEndList = mentoringStatusRepository.getMentorMentoringEndList(memberId);
                List<MentoringStatusRes> getMentoringWaitList = mentoringStatusRepository.getMentorMentoringWaitList(memberId);

                GetMentoringStatus getMentorMentoring = new GetMentoringStatus(getMentoringNowList, getMentoringEndList, getMentoringWaitList);
            return getMentorMentoring;}
            else if(Objects.equals(profile, "mentee")){
                List<NowMentoringRes> getMentoringNowList = mentoringStatusRepository.getMenteeMentoringNowList(memberId);
                for(NowMentoringRes now:getMentoringNowList){
                    System.out.println(now.getMentoringId());
                }
                List<EndMentoringRes> getMentoringEndList = mentoringStatusRepository.getMenteeMentoringEndList(memberId);
                List<MentoringStatusRes> getMentoringWaitList = mentoringStatusRepository.getMenteeMentoringWaitList(memberId);
                for(MentoringStatusRes now:getMentoringWaitList){
                    System.out.println(now.getMentoringId());
                }

                GetMentoringStatus getMenteeMentoring = new GetMentoringStatus(getMentoringNowList, getMentoringEndList, getMentoringWaitList);


                return getMenteeMentoring;}
            else{
                throw new BaseException(DATABASE_ERROR);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
