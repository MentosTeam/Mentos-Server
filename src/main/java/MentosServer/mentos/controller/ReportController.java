package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.model.dto.GetReportRes;
import MentosServer.mentos.model.dto.PostReportReq;
import MentosServer.mentos.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static MentosServer.mentos.config.Constant.endMentoringBody;
import static MentosServer.mentos.config.Constant.endMentoringTitle;

@RestController
public class ReportController {

	private final ReportService reportService;
	
	@Autowired
	public ReportController(ReportService reportService) {
		this.reportService = reportService;
	}
	
	/**
	 * Report 등록
	 * 성공적으로 수행된다면 1 반환
	 * @param req
	 * @return
	 */
	@PostMapping("/report")
	public BaseResponse<Integer> postReport(@RequestBody PostReportReq req){
		try {
			int flag= reportService.postReport(req);
			if(flag==2){
				reportService.sendMessage(req.getMentoringId(),endMentoringTitle,endMentoringBody,2);//멘티에게 보내기
			}
			return new BaseResponse<>(flag);
		} catch (BaseException exception) {
			return new BaseResponse<>((exception.getStatus()));
		}
	}
	
	/**
	 * Report 검색
	 * @param mentoringId
	 * @return
	 */
	@GetMapping("/report")
	public BaseResponse<GetReportRes> getReport(@RequestParam int mentoringId){
		try{
			GetReportRes res = new GetReportRes(reportService.getReport(mentoringId));
			return new BaseResponse<>(res);
		} catch (BaseException exception) {
			return new BaseResponse<>(exception.getStatus());
		}
	}
}
