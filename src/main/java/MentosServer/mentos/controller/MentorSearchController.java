package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.model.dto.GetMentorSearchReq;
import MentosServer.mentos.model.dto.GetMentorSearchRes;
import MentosServer.mentos.service.MentorSearchService;
import MentosServer.mentos.utils.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MentorSearchController {
	
	private final JwtService jwtService;
	private final MentorSearchService mentorSearchService;
	
	@Autowired
	public MentorSearchController(JwtService jwtService, MentorSearchService mentorSearchService) {
		this.jwtService = jwtService;
		this.mentorSearchService = mentorSearchService;
	}
	
	@GetMapping("/mentor/search")
	public BaseResponse<GetMentorSearchRes> mentorSearch(@ModelAttribute GetMentorSearchReq req){
		try {
			// jwt에서 memberId 뽑아내기
			String memberId = Integer.toString(jwtService.getMemberId());
			// 만약 전공 flag가 비어있다면 memberId를 통해 default값 생성
			if(req.getMajorFlag().size() == 0) {
				// memberId로 선택했던 전공 가져오기
				String major = mentorSearchService.getMajorById(memberId);
				req.getMajorFlag().add(major);
			}
			// memberId로 menti의 학교 가져오기
			String schoolId = mentorSearchService.getSchoolById(memberId);
			// 검색하기
			GetMentorSearchRes res = new GetMentorSearchRes(mentorSearchService.mentorSearch(req, schoolId));
			return new BaseResponse<>(res);
		} catch (BaseException exception) {
			return new BaseResponse<>((exception.getStatus()));
		}
	}
}
