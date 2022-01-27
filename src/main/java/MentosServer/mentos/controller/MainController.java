package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.model.dto.GetMenteeMainRes;
import MentosServer.mentos.model.dto.GetMentorMainRes;
import MentosServer.mentos.service.MainService;
import MentosServer.mentos.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

	private final JwtService jwtService;
	private final MainService mainService;
	
	@Autowired
	public MainController(JwtService jwtService, MainService mainService) {
		this.jwtService = jwtService;
		this.mainService = mainService;
	}
	
	@GetMapping("/mentor/main")
	public BaseResponse<GetMentorMainRes> mentorMain(){
		try {
			// jwt에서 memberId 뽑아내기
			String memberId = Integer.toString(jwtService.getMemberId());
			// 멘티 리스트 반환
			GetMentorMainRes res = mainService.getMenteeList(memberId);
			return new BaseResponse<>(res);
		} catch (BaseException exception) {
			return new BaseResponse<>((exception.getStatus()));
		}
	}
	
	@GetMapping("/mentee/main")
	public BaseResponse<GetMenteeMainRes> menteeMain(){
		try {
			// jwt에서 memberId 뽑아내기
			String memberId = Integer.toString(jwtService.getMemberId());
			// 멘토 리스트 반환
			GetMenteeMainRes res = mainService.getMentorList(memberId);
			return new BaseResponse<>(res);
		} catch (BaseException exception) {
			return new BaseResponse<>((exception.getStatus()));
		}
	}
}
