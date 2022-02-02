package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.config.BaseResponseStatus;
import MentosServer.mentos.model.dto.PostNewPwReq;
import MentosServer.mentos.model.dto.PostPasswordReq;
import MentosServer.mentos.service.PasswordService;
import MentosServer.mentos.utils.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static MentosServer.mentos.config.BaseResponseStatus.*;

@Slf4j
@RestController
@RequestMapping("/members")
public class PasswordController {

	private final PasswordService passwordService;
	private final JwtService jwtService;

	@Autowired
	public PasswordController(PasswordService passwordService, JwtService jwtService){
		this.passwordService = passwordService;
		this.jwtService = jwtService;
	}

	/**
	 *
	 * @param postPasswordReq
	 * @return
	 */
	@PostMapping("/pwInquiry")
	public BaseResponse sendPwEmail(@Valid @RequestBody PostPasswordReq postPasswordReq, BindingResult br){
		//validation 추가
		if(br.hasErrors()){
			String errorName = br.getAllErrors().get(0).getDefaultMessage();
			return new BaseResponse<>(BaseResponseStatus.of(errorName));
		}
		try {
			//임시 비밀번호 보내기
			passwordService.sendEmail(postPasswordReq);
			return new BaseResponse<>(SUCESS_SEND_PASSWORD);
		} catch (BaseException exception) {
			return new BaseResponse<>((exception.getStatus()));
		}
	}

	/**
	 * 설정에서
	 * 임시 비밀번호가 맞는지 확인
	 * 새로운 비밀번호로 설정
	 * @return
	 */
	@PostMapping("/pwChange")
	public BaseResponse changePw(@Valid @RequestBody PostNewPwReq newPwReq, BindingResult br){
		//validation 추가
		if(br.hasErrors()){
			String errorName = br.getAllErrors().get(0).getDefaultMessage();
			return new BaseResponse<>(BaseResponseStatus.of(errorName));
		}

		try {
			int memberIdByJwt = jwtService.getMemberId();
			passwordService.changePassword(newPwReq, memberIdByJwt);
			return new BaseResponse<>(SUCESS_CHANGE_PASSWORD);
		} catch (BaseException exception) {
			return new BaseResponse<>((exception.getStatus()));
		}
	}

}
