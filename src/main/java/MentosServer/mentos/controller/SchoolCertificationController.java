package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.model.dto.GetSchoolCertificationReq;
import MentosServer.mentos.model.dto.GetSchoolCertificationRes;
import MentosServer.mentos.service.SchoolCertificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static MentosServer.mentos.config.BaseResponseStatus.*;
import static MentosServer.mentos.utils.ValidationRegex.isRegexEmail;

@Slf4j
@RestController
public class SchoolCertificationController {

	private final SchoolCertificationService schoolCertificationService;

	@Autowired
	public SchoolCertificationController(SchoolCertificationService schoolCertificationService){
		this.schoolCertificationService = schoolCertificationService;
	}

	@GetMapping("/schoolCertification")
	public BaseResponse<GetSchoolCertificationRes> schoolCertification(@RequestBody GetSchoolCertificationReq req){
		// email에 값이 존재하는지, 빈 값으로 요청하지는 않았는지 검사합니다. 빈값으로 요청했다면 에러 메시지를 보냅니다.
		if (req.getEmail() == null) {
			return new BaseResponse<>(GET_USERS_EMPTY_EMAIL);
		}
		//이메일 정규표현: 입력받은 이메일이 email@domain.xxx와 같은 형식인지 검사합니다. 형식이 올바르지 않다면 에러 메시지를 보냅니다.
		if (!isRegexEmail(req.getEmail())) {
			return new BaseResponse<>(GET_USERS_INVALID_EMAIL);
		}
		// 중복 확인: 해당 이메일을 가진 유저가 있는지 확인합니다. 중복될 경우, 에러 메시지를 보냅니다.
		if(!schoolCertificationService.checkEmail(req)) {
			return new BaseResponse<>(GET_USERS_EXISTS_EMAIL);
		}
		try {
			// 학교 이메일이 맞는지 검사
			schoolCertificationService.cmpSchoolEmail(req);
			// 인증번호 메일 전송
			String randomNumber = schoolCertificationService.sendEmail(req.getEmail());
			return new BaseResponse<>(new GetSchoolCertificationRes(randomNumber));
		} catch (BaseException exception) {
			return new BaseResponse<>((exception.getStatus()));
		}
	}
}
