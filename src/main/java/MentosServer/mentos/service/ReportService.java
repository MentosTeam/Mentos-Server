package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.model.dto.GetReportRes;
import MentosServer.mentos.model.dto.PostReportReq;
import MentosServer.mentos.model.dto.ReportList;
import MentosServer.mentos.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static MentosServer.mentos.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ReportService {

	private final ReportRepository reportRepository;
	
	@Autowired
	public ReportService(ReportRepository reportRepository) {
		this.reportRepository = reportRepository;
	}
	
	public int postReport(PostReportReq req) throws BaseException {
		try{
			int mentoringId = req.getMentoringId();
			String report = req.getReport();
			// 글 등록
			reportRepository.postReport(mentoringId, report);
			// Mentoring Count 감소시키기
			int finFlag = reportRepository.subMentoringCnt(mentoringId);
			if(finFlag == 0) { // 마지막 멘토링이라면 종료로 바꾸기
				reportRepository.stopMentoring(mentoringId);
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
