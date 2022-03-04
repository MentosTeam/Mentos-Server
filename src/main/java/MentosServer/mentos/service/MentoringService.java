package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.model.domain.Mentoring;
import MentosServer.mentos.model.dto.*;
import MentosServer.mentos.repository.MentoringRepository;
import MentosServer.mentos.repository.NoticeRepository;
import MentosServer.mentos.utils.fcm.FirebaseCloudMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static MentosServer.mentos.config.BaseResponseStatus.*;
import static MentosServer.mentos.config.Constant.*;

@Slf4j
@Service
public class MentoringService implements SendFcm{
    private final FcmTokenService fcmTokenService;
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final MentoringRepository mentoringRepository;
    private final NoticeRepository noticeRepository;

    @Autowired
    public MentoringService(FcmTokenService fcmTokenService, FirebaseCloudMessageService firebaseCloudMessageService, MentoringRepository mentoringRepository, NoticeRepository noticeRepository) {
        this.fcmTokenService = fcmTokenService;
        this.firebaseCloudMessageService = firebaseCloudMessageService;
        this.mentoringRepository = mentoringRepository;
        this.noticeRepository = noticeRepository;
    }
    @Override
    public void sendMessage(int mentoringId,String title, String body,int receiverFlag) throws BaseException {
        // 푸시 알림 보내기
        //log.info("리스트 얻어오기");
        Mentoring mentoring = mentoringRepository.getMentoring(mentoringId);
        int memberId;
        if(receiverFlag==1){
            memberId=mentoring.getMentoringMentoId();
            noticeRepository.setNotification(mentoring.getMentoringMentoId(),1,title+"\n"+body); //멘토 알림 DB 저장
        }else{
            memberId=mentoring.getMentoringMentiId();
            noticeRepository.setNotification(mentoring.getMentoringMentiId(),2,title+"\n"+body); //멘티 알림 DB 저장
        }
        System.out.println("memberId = " + memberId);
        List<String> memberToken = fcmTokenService.selectUserDeviceTokenByIdx(memberId);
        if (memberToken.isEmpty()) {
            log.error("Cannot found member device token");
            throw new BaseException(EMPTY_MEMBER_DEVICE_TOKEN);
        }
        firebaseCloudMessageService.sendMessageTo(memberToken,title,body,receiverFlag);
    }

    //멘토링 등록
    public PostMentoringRes createMentoring(PostMentoringReq postMentoringReq) throws BaseException {
        if (mentoringRepository.checkMentoring(postMentoringReq) == 1) { // 멘토링 중복 신청 확인
            throw new BaseException(POST_MENTORING_DUPLICATED_MENTORING);
        }

        PostMentoringRes postMentoringRes;
        try {
            int mentoringId = mentoringRepository.createMentoring(postMentoringReq); //멘토링 생성
            postMentoringRes = new PostMentoringRes(mentoringId, postMentoringReq.getMentoId(), postMentoringReq.getMentiId());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
        //푸시 알림 보내기
        //log.info("MentoringService.sendMessage 호출");
        return postMentoringRes;
    }

    //멘토링 수락/거절
    public PostAcceptMentoringRes acceptMentoring(int mentoringId, int mentoId, Boolean acceptance) throws BaseException{
        if(mentoringRepository.checkMentoringByMento(mentoringId, mentoId) == 0){ //멘토링 요청 존재 확인
            throw new BaseException(POST_INVALID_MENTORING);
        }

        try{
            PostAcceptMentoringRes postAcceptMentoringRes = new PostAcceptMentoringRes(mentoringId, mentoId, "");
            Mentoring mentoring = mentoringRepository.getMentoring(mentoringId);
            if(acceptance){
                try{ //기존 멘토링이 진행 중인 경우, 새로 수락한 멘토링을 기존 멘토링과 합하고 새로 수락한 멘토링 요청 삭제
                    int beforeMentoringId = mentoringRepository.checkProceedingMentoring(mentoring.getMentoringMentoId(), mentoring.getMentoringMentiId(), mentoring.getMajorCategoryId());

                    mentoringRepository.addMentoring(mentoring, beforeMentoringId);
                    mentoringRepository.deleteMentoring(mentoring.getMentoringId());
                } catch (Exception e){ //해당 멘토와 멘토링 처음 진행하는 경우
                    mentoringRepository.acceptMentoring(mentoringId);
                }

                postAcceptMentoringRes.setStatus("성공적으로 멘토링 요청을 수락했습니다.");

            }
            else {
                mentoringRepository.deleteMentoring(mentoringId);
                postAcceptMentoringRes.setStatus("성공적으로 멘토링 요청을 거절했습니다.");
            }
            return postAcceptMentoringRes;
        } catch (Exception e){
            if(e instanceof BaseException){
                throw (BaseException) e;
            }
            else {
                throw new BaseException(DATABASE_ERROR);
            }
        }
    }

    //멘토링 강제 종료
    public PatchStopMentoringRes stopMentoring(int mentoringId, int mentiId) throws BaseException{
        if(mentoringRepository.checkMentoringByMenti(mentoringId, mentiId, 1) == 0){
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

    //멘토링 요청 취소
    public void deleteMentoring(int mentoringId, int mentiId) throws BaseException{
        if(mentoringRepository.checkMentoringByMenti(mentoringId, mentiId, 0) == 0){
            throw new BaseException(PATCH_INVALID_MENTORING);
        }

        try{
            if(mentoringRepository.deleteMentoring(mentoringId) == 0){
                throw new BaseException(FAILDE_TO_DELETEMENTORING);
            }
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //멘토&멘티 닉네임 조회
    public GetNicknameRes getNickname(int mentoId, int mentiId) throws BaseException{
        try{
            GetNicknameRes getNicknameRes = new GetNicknameRes();
            getNicknameRes.setMentoNickname(mentoringRepository.getNickname(mentoId));
            getNicknameRes.setMentiNickname(mentoringRepository.getNickname(mentiId));

            return getNicknameRes;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}