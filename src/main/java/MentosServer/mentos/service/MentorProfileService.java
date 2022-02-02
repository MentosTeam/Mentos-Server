package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.model.domain.Post;
import MentosServer.mentos.model.domain.Review;
import MentosServer.mentos.model.dto.GetMentorProfileRes;
import MentosServer.mentos.model.dto.MentorProfileDto;
import MentosServer.mentos.model.dto.MentosCountDto;
import MentosServer.mentos.model.dto.PostWithImageDto;
import MentosServer.mentos.repository.MentorProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static MentosServer.mentos.config.BaseResponseStatus.DATABASE_ERROR;
import static MentosServer.mentos.config.BaseResponseStatus.INVALID_MENTORPROFILE;

@Service
public class MentorProfileService {
    private final MentorProfileRepository mentorProfileRepository;

    @Autowired
    public MentorProfileService(MentorProfileRepository mentorProfileRepository){
        this.mentorProfileRepository = mentorProfileRepository;
    }

    //멘토 프로필 조회
    @Transactional(readOnly = true)
    public GetMentorProfileRes getMentorProfile(int memberId) throws BaseException{
        if(mentorProfileRepository.checkMentorProfile(memberId) == 0){
            throw new BaseException(INVALID_MENTORPROFILE);
        }

        try{
            MentorProfileDto mentorProfileDto = mentorProfileRepository.getBasicInfoByMentor(memberId);
            String schoolName = mentorProfileRepository.getSchoolName(mentorProfileDto.getSchoolId());
            int numOfMentoring = mentorProfileRepository.getNumOfMentoringByMentor(memberId);
            List<PostWithImageDto> posts = mentorProfileRepository.getPostsByMemberId(memberId);
            List<Review> reviews = mentorProfileRepository.getReviewsOnMentor(memberId);
            List<MentosCountDto> mentosCount = mentorProfileRepository.getMentosCount(memberId);

            GetMentorProfileRes getMentorProfileRes = new GetMentorProfileRes(mentorProfileDto, schoolName, numOfMentoring, posts, reviews, mentosCount);
            return getMentorProfileRes;

        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
