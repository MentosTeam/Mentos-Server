package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.model.dto.PostReportReq;
import MentosServer.mentos.model.dto.ReportList;
import MentosServer.mentos.repository.ReportRepository;
import MentosServer.mentos.utils.fcm.FirebaseCloudMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static MentosServer.mentos.config.BaseResponseStatus.*;

@Service
@Transactional
public class ReportService {
	private final FcmTokenService fcmTokenService;
	private final FirebaseCloudMessageService firebaseCloudMessageService;
	private final ReportRepository reportRepository;
	
	@Autowired
	public ReportService(FcmTokenService fcmTokenService, FirebaseCloudMessageService firebaseCloudMessageService, ReportRepository reportRepository) {
		this.fcmTokenService = fcmTokenService;
		this.firebaseCloudMessageService = firebaseCloudMessageService;
		this.reportRepository = reportRepository;
	}

	
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
				//멘티Id가져오기
				int menteeId = reportRepository.getMentoringMentee(mentoringId);
				List<String> menteeToken = fcmTokenService.selectUserDeviceTokenByIdx(menteeId);//멘티의 디바이스 토큰 정보 가져오기
				firebaseCloudMessageService.sendMessageTo(menteeToken,"멘토링이 종료되었습니다-!","⭐️멘토링 별점 및 후기 남기기 잊지마세요✏️",2);//멘티에게 전송
				return 2;
			}
			return 1;
		} catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
			throw new BaseException(DATABASE_ERROR);
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
