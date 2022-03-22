package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
	
	@GetMapping("/deployTest")
	public BaseResponse<String> deployTest(){
		return new BaseResponse<>("deployTest");
	}
}
