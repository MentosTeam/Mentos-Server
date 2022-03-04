package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.model.domain.Mentoring;
import MentosServer.mentos.model.dto.PostReportReq;
import MentosServer.mentos.model.dto.ReportList;
import MentosServer.mentos.repository.NoticeRepository;
import MentosServer.mentos.repository.ReportRepository;
import MentosServer.mentos.utils.fcm.FirebaseCloudMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static MentosServer.mentos.config.BaseResponseStatus.*;
import static MentosServer.mentos.config.Constant.endMentoringBody;
import static MentosServer.mentos.config.Constant.endMentoringTitle;

@Service
@Transactional
public class ReportService implements SendFcm{
	private final FcmTokenService fcmTokenService;
	private final FirebaseCloudMessageService firebaseCloudMessageService;
	private final ReportRepository reportRepository;
	private final NoticeRepository noticeRepository;
	
	@Autowired
	public ReportService(FcmTokenService fcmTokenService, FirebaseCloudMessageService firebaseCloudMessageService, ReportRepository reportRepository, NoticeRepository noticeRepository) {
		this.fcmTokenService = fcmTokenService;
		this.firebaseCloudMessageService = firebaseCloudMessageService;
		this.reportRepository = reportRepository;
		this.noticeRepository = noticeRepository;
	}

	@Override
	public void sendMessage(int mentoringId,String title, String body,int receiverFlag) throws BaseException {
		// 푸시 알림 보내기

		//멘티Id가져오기
		int memberId = reportRepository.getMentoringMentee(mentoringId);
		//알림 저장하기
		noticeRepository.setNotification(memberId,2,title +"/n"+body); //멘티 알림 DB 저장
		//디바이스 토큰 가져오기
		List<String> memberToken = fcmTokenService.selectUserDeviceTokenByIdx(memberId);
		if (memberToken.isEmpty()) {
			throw new BaseException(EMPTY_MEMBER_DEVICE_TOKEN);
		}
		firebaseCloudMessageService.sendMessageTo(memberToken,title,body,receiverFlag);
	}

	@Transactional(rollbackFor = {Exception.class,BaseException.class})
	public int postReport(PostReportReq req) throws BaseException {
		try{
			int mentoringId = req.getMentoringId();
			String report = req.getReport();
			// 글 등록
			reportRepository.postReport(mentoringId, report);
			// Mentoring Count 확인 (Report 개수로)
			int finFlag = reportRepository.getMentoringCnt(mentoringId);
			int finCnt = reportRepository.getFinCnt(mentoringId);
			if(finFlag == finCnt) { // 마지막 멘토링이라면 종료로 바꾸기
				reportRepository.stopMentoring(mentoringId);
				reportRepository.minusMenteeMentos(mentoringId); //멘티 멘토스 감소
				reportRepository.addMentorMentos(mentoringId); //멘토 멘토스 증가
				return 2;
			}
			return 1;
		} catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
			if(exception instanceof BaseException){
				throw (BaseException) exception;
			}
			else {
				throw new BaseException(DATABASE_ERROR);
			}
		}
	}
	
	public List<ReportList> getReport(int mentoringId) throws BaseException {
		try{
			List<ReportList> reports = reportRepository.getReport(mentoringId);
			for(ReportList report : reports){
				report.setCreateAt(returnDateOnly(report.getCreateAt()));
			}
			return reports;
		} catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
			throw new BaseException(DATABASE_ERROR);
		}
	}
	
	public String returnDateOnly(String str){
		String ret = "";
		for(int i=0; i<10; i++){
			char c = str.charAt(i);
			ret += c == '-' ? '/' : c;
		}
		return ret;
	}
	
}
