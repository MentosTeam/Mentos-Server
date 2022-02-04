package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.model.domain.Review;
import MentosServer.mentos.model.dto.*;
import MentosServer.mentos.repository.MenteeProfileRepository;
import MentosServer.mentos.repository.MentorProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static MentosServer.mentos.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class MyPageService {
    private final MentorProfileRepository mentorProfileRepository;
    private final MenteeProfileRepository menteeProfileRepository;

    @Autowired
    public MyPageService(MentorProfileRepository mentorProfileRepository, MenteeProfileRepository menteeProfileRepository){
        this.mentorProfileRepository = mentorProfileRepository;
        this.menteeProfileRepository = menteeProfileRepository;
    }

    // 내 정보 화면 조회
    @Transactional(readOnly = true)
    public GetMyPageRes getMyPage(int memberId) throws BaseException {
        try{
            GetMentorProfileRes getMentorProfileRes = null;
            GetMenteeProfileRes getMenteeProfileRes = null;

            if(mentorProfileRepository.checkMentorProfile(memberId) == 1){ // 멘토 프로필 존재 여부 확인
                MentorProfileDto mentorProfileDto = mentorProfileRepository.getBasicInfoByMentor(memberId);
                String schoolName = mentorProfileRepository.getSchoolName(mentorProfileDto.getSchoolId());
                int numOfMentoring = mentorProfileRepository.getNumOfMentoringByMentor(memberId);
                List<PostWithImageDto> posts = mentorProfileRepository.getPostsByMemberId(memberId);
                List<Review> reviews = mentorProfileRepository.getReviewsOnMentor(memberId);
                List<MentosCountDto> mentosCount = mentorProfileRepository.getMentosCount(memberId);

                getMentorProfileRes = new GetMentorProfileRes(mentorProfileDto, schoolName, numOfMentoring, posts, reviews, mentosCount);
            }

            if(menteeProfileRepository.checkMenteeProfile(memberId) == 1){ // 멘티 프로필 존재 여부 확인
                MenteeProfileDto profileDto = menteeProfileRepository.getBasicInfoByMentee(memberId);
                String schoolName = menteeProfileRepository.getSchoolName(profileDto.getSchoolId());
                int numOfMentoring = menteeProfileRepository.getNumOfMentoringByMentee(memberId);


                getMenteeProfileRes = new GetMenteeProfileRes(profileDto, schoolName, numOfMentoring);
            }

            GetMyPageRes getMyPageRes = new GetMyPageRes(getMentorProfileRes, getMenteeProfileRes);
            return getMyPageRes;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
