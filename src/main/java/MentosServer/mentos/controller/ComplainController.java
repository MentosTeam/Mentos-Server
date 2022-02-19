package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.model.dto.GetComplainReq;
import MentosServer.mentos.model.dto.MailDto;
import MentosServer.mentos.service.ComplainService;
import MentosServer.mentos.service.MailService;
import MentosServer.mentos.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ComplainController {

	private ComplainService complainService;
	private MailService mailService;
	private JwtService jwtService;
	
	private String email = "teammentos0214@gmail.com";
	
	@Autowired
	public ComplainController(ComplainService complainService, MailService mailService, JwtService jwtService) {
		this.complainService = complainService;
		this.mailService = mailService;
		this.jwtService = jwtService;
	}
	
	@PostMapping("/complain")
	public BaseResponse<String> getCategory(@RequestBody GetComplainReq req){
		try {
			// jwt에서 memberId 뽑아내기
			int memberId = jwtService.getMemberId();
			// 메일 내용 생성
			String text = complainService.makeMailText(memberId, req);
			MailDto mailDto = new MailDto(email, "신고", text);
			mailService.mailSend(mailDto);
			return new BaseResponse<String>("메일 전송에 성공하였습니다.");
		} catch (BaseException exception) {
			return new BaseResponse<>((exception.getStatus()));
		}
	}
}
