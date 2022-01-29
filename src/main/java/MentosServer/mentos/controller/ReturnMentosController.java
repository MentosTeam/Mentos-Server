package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.model.dto.GetReturnMentosRes;
import MentosServer.mentos.service.ReturnMentosService;
import MentosServer.mentos.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReturnMentosController {
	
	private final JwtService jwtService;
	private final ReturnMentosService returnMentosService;
	
	@Autowired
	public ReturnMentosController(JwtService jwtService, ReturnMentosService returnMentosService) {
		this.jwtService = jwtService;
		this.returnMentosService = returnMentosService;
	}
	
	@GetMapping("/mentos")
	public BaseResponse<GetReturnMentosRes> returnMentos(){
		try{
			String memberId = Integer.toString(jwtService.getMemberId());
			return new BaseResponse<>(new GetReturnMentosRes(returnMentosService.returnMentos(memberId)));
		} catch (BaseException exception) {
			return new BaseResponse<>((exception.getStatus()));
		}
	}
}
