package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.model.dto.GetMenteeSearchReq;
import MentosServer.mentos.model.dto.GetMenteeSearchRes;
import MentosServer.mentos.service.MenteeSearchService;
import MentosServer.mentos.utils.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MenteeSearchController {
	
	private final JwtService jwtService;
	private final MenteeSearchService menteeSearchService;
	
	@Autowired
	public MenteeSearchController(JwtService jwtService, MenteeSearchService menteeSearchService) {
		this.jwtService = jwtService;
		this.menteeSearchService = menteeSearchService;
	}
	
	@GetMapping("/mentor/search")
	public BaseResponse<GetMenteeSearchRes> menteeSearch(@ModelAttribute GetMenteeSearchReq req){
		try {
			// jwt에서 memberId 뽑아내기
			String memberId = Integer.toString(jwtService.getMemberId());
			// 만약 전공 flag가 비어있다면 memberId를 통해 default값 생성
			if(req.getMajorFlag().size() == 0) {
				// memberId로 선택했던 전공 가져오기
				String major = menteeSearchService.getMajorById(memberId);
				req.getMajorFlag().add(major);
			}
			// memberId로 mento의 학교 가져오기
			String schoolId = menteeSearchService.getSchoolById(memberId);
			// 검색하기
			GetMenteeSearchRes res = new GetMenteeSearchRes(menteeSearchService.menteeSearch(memberId, req, schoolId));
			return new BaseResponse<>(res);
		} catch (BaseException exception) {
			return new BaseResponse<>((exception.getStatus()));
		}
	}
}
