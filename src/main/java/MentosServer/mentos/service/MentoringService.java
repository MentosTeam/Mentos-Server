package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.model.domain.Mentoring;
import MentosServer.mentos.model.dto.*;
import MentosServer.mentos.repository.MentoringRepository;
import MentosServer.mentos.utils.fcm.FirebaseCloudMessageService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static MentosServer.mentos.config.BaseResponseStatus.*;

@Slf4j
@Service
public class MentoringService {
    private final FcmTokenService fcmTokenService;
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final MentoringRepository mentoringRepository;

    @Autowired
    public MentoringService(FcmTokenService fcmTokenService, FirebaseCloudMessageService firebaseCloudMessageService, MentoringRepository mentoringRepository) {
        this.fcmTokenService = fcmTokenService;
        this.firebaseCloudMessageService = firebaseCloudMessageService;
        this.mentoringRepository = mentoringRepository;
    }
    public void sendMessage(int memberId,String title, String body,int senderFlag) throws BaseException {
        // 푸시 알림 보내기
        //log.info("리스트 얻어오기");
        List<String> memberToken = fcmTokenService.selectUserDeviceTokenByIdx(memberId);
        if (memberToken.isEmpty()) {
            log.error("Cannot found member device token");
            throw new BaseException(EMPTY_MEMBER_DEVICE_TOKEN);
        }
        firebaseCloudMessageService.sendMessageTo(memberToken,title,body,senderFlag);
    }

    //멘토링 등록
    public PostMentoringRes createMentoring(PostMentoringReq postMentoringReq) throws BaseException {
        if (mentoringRepository.checkMentoring(postMentoringReq) == 1) { // 멘토링 중복 신청 확인
            throw new BaseException(POST_MENTORING_DUPLICATED_MENTORING);
        }
        String title = "멘토링 요청이 도착했어요 \uD83C\uDF89";
        String body = "멘토링 현황에서 수락 여부를 알려주세요-!";
        PostMentoringRes postMentoringRes;
        try {
            int mentoringId = mentoringRepository.createMentoring(postMentoringReq);

            postMentoringRes = new PostMentoringRes(mentoringId, postMentoringReq.getMentoId(), postMentoringReq.getMentiId());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
        //푸시 알림 보내기
        //log.info("MentoringService.sendMessage 호출");
        sendMessage(postMentoringReq.getMentoId(), title, body, 1);//멘토에게
        return postMentoringRes;
    }

    //멘토링 수락/거절
    @Transactional(rollbackFor = Exception.class)
    public PostAcceptMentoringRes acceptMentoring(int mentoringId, int mentoId, Boolean acceptance) throws BaseException{
        if(mentoringRepository.checkMentoringByMento(mentoringId, mentoId) == 0){ //멘토링 요청 존재 확인
            throw new BaseException(POST_INVALID_MENTORING);
        }

        String title="";
        String body="";
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
                title="\uD83C\uDF89 멘토가 멘토링을 수락했어요 \uD83C\uDF89 ";
                body="멘토링이 시작되었습니다-!\n" +
                        "\n" +
                        "오늘도 멘토-쓰를 통해\n" +
                        "한 층 더 발전된 하루를 만들기 바랍니다!";
            }
            else {
                mentoringRepository.deleteMentoring(mentoringId);
                postAcceptMentoringRes.setStatus("성공적으로 멘토링 요청을 거절했습니다.");
                title="멘토가 멘토링 요청을 수락하지 않았어요";
                body="괜찮아요 \uD83D\uDE0A \n" +
                        "멘토-쓰 찾기에서 나에게 맞는 멘토를 다시 찾아보아요.";
            }
            sendMessage(mentoring.getMentoringMentiId(),title,body,2); //멘티에게
            return postAcceptMentoringRes;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
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