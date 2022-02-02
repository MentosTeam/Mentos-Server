package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.model.dto.GetMenteeProfileRes;
import MentosServer.mentos.model.dto.ProfileDto;
import MentosServer.mentos.repository.MenteeProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static MentosServer.mentos.config.BaseResponseStatus.DATABASE_ERROR;
import static MentosServer.mentos.config.BaseResponseStatus.INVALID_MENTEEPROFILE;

@Service
public class MenteeProfileService {
    private final MenteeProfileRepository menteeProfileRepository;

    @Autowired
    public MenteeProfileService(MenteeProfileRepository menteeProfileRepository){
        this.menteeProfileRepository = menteeProfileRepository;
    }

    //멘티 프로필 조회
    @Transactional(readOnly = true)
    public GetMenteeProfileRes getMenteeProfile(int memberId) throws BaseException {
        if(menteeProfileRepository.checkMenteeProfile(memberId) == 0){
            throw new BaseException(INVALID_MENTEEPROFILE);
        }
        try{
            ProfileDto profileDto = menteeProfileRepository.getBasicInfoByMentee(memberId);
            String schoolName = menteeProfileRepository.getSchoolName(profileDto.getSchoolId());
            int numOfMentoring = menteeProfileRepository.getNumOfMentoringByMentee(memberId);


            GetMenteeProfileRes getMenteeProfileRes = new GetMenteeProfileRes(profileDto, schoolName, numOfMentoring);
            return getMenteeProfileRes;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
