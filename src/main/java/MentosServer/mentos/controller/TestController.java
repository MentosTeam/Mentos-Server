package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	@GetMapping("/deployTest")
	public BaseResponse<String> deployTest(){
		return new BaseResponse<>("deployTest");
	}
}
