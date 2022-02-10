package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.model.dto.GetMentoringStatus;
import MentosServer.mentos.model.dto.MentoringStatusRes;
import MentosServer.mentos.repository.LoginRepository;
import MentosServer.mentos.repository.MentoringStatusRepository;
import MentosServer.mentos.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static MentosServer.mentos.config.BaseResponseStatus.DATABASE_ERROR;
import static MentosServer.mentos.config.BaseResponseStatus.INVALID_PATH_VARIABLE;

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
            List<MentoringStatusRes> getMentoringNowList = mentoringStatusRepository.getMentorMentoringNowList(memberId);
            List<MentoringStatusRes> getMentoringEndList = mentoringStatusRepository.getMentorMentoringEndList(memberId);
            GetMentoringStatus getMentorMentoring = new GetMentoringStatus(getMentoringNowList, getMentoringEndList);
            return getMentorMentoring;}
            else if(Objects.equals(profile, "mentee")){
                List<MentoringStatusRes> getMentoringNowList = mentoringStatusRepository.getMenteeMentoringNowList(memberId);
                List<MentoringStatusRes> getMentoringEndList = mentoringStatusRepository.getMenteeMentoringEndList(memberId);
                GetMentoringStatus getMenteeMentoring = new GetMentoringStatus(getMentoringNowList, getMentoringEndList);

                return getMenteeMentoring;}
            else{
                throw new BaseException(DATABASE_ERROR);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
