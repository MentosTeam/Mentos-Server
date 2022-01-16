package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.model.dto.GetSchoolCertificationReq;
import MentosServer.mentos.model.dto.MailDto;
import MentosServer.mentos.repository.SchoolCertificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

import static MentosServer.mentos.config.BaseResponseStatus.*;

@Slf4j
@Service
public class SchoolCertificationService {
	
	
	private SchoolCertificationRepository schoolCertificationRepository;
	private MailService mailService;
	
	@Autowired
	public void SchoolCertificationService(SchoolCertificationRepository schoolCertificationRepository, MailService mailService){
		this.schoolCertificationRepository = schoolCertificationRepository;
		this.mailService = mailService;
	}
	
	public boolean checkEmail(GetSchoolCertificationReq req) {
		if (schoolCertificationRepository.checkEmail(req.getEmail()) == 1) {
			return false;
		}
		else return true;
	}
	
	public void cmpSchoolEmail(GetSchoolCertificationReq req) throws BaseException{
		try{
			// 학교 이메일이 맞는지 확인
			if(!parseEmail(req.getEmail()).equals(schoolCertificationRepository.getSchoolEmail(req.getSchool()))){
				throw new BaseException(INVALID_SCHOOL_EMAIL); // 학교 이메일 일치하지 않음
			}
		} catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
			throw new BaseException(DATABASE_ERROR);
		}
	}
	
	public String parseEmail(String email){
		// email 뒷 부분만 파싱해서 반환
		String ret = "";
		boolean flag = false;
		for(int i=0; i<email.length(); i++){
			if(email.charAt(i) == '@') {
				flag = true;
				continue;
			}
			if(flag) {
				ret += email.charAt(i);
			}
		}
		return ret;
	}
	
	public String sendEmail(String email) throws BaseException {
		try{
			String randomNumber = createRandomNumber();
			// email 보내기
			MailDto mailDto = new MailDto(email, "Mentos 인증번호", randomNumber);
			mailService.mailSend(mailDto);
			return randomNumber;
		} catch (Exception exception) {
			throw new BaseException(MAIL_SEND_ERROR);
		}
	}
	
	public String createRandomNumber(){
		// random certification number 생성
		long seed = System.currentTimeMillis(); // 현재 시간을 seed로 (호출 시마다 seed 변경)
		Random rand = new Random(seed);
		// 0 ~ 9 사이의 숫자를 4번 랜덤하게 뽑아서 String으로 변환
		String num = "";
		for(int i = 0; i < 4; i++) {
			num += Integer.toString(rand.nextInt(10));
		}
		return num;
	}
}
