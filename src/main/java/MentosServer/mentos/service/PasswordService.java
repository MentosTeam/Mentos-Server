package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.secret.Secret;
import MentosServer.mentos.model.domain.Member;
import MentosServer.mentos.model.dto.MailDto;
import MentosServer.mentos.model.dto.PostNewPwReq;
import MentosServer.mentos.model.dto.PostPasswordReq;
import MentosServer.mentos.repository.PasswordRepository;
import MentosServer.mentos.utils.AES128;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

import static MentosServer.mentos.config.BaseResponseStatus.*;

@Service
@Slf4j
public class PasswordService implements MailCheckService<PostPasswordReq>{

    private final PasswordRepository passwordRepository;
    private final MailService mailService;

    @Autowired
    public PasswordService(PasswordRepository passwordRepository, MailService mailService) {
        this.passwordRepository = passwordRepository;
        this.mailService = mailService;
    }

    /**
     * 이메일이 DB에 있으면 true, 없으면 false
     * @param req
     * @return
     */
    @Override
    public boolean checkEmail(PostPasswordReq req) {
        if(passwordRepository.checkEmail(req)==1){
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = {BaseException.class,Exception.class})
    @Override
    public String sendEmail(PostPasswordReq req) throws BaseException {
        //validation - 이메일이 존재하는지 확인
        if(!checkEmail(req)){
            throw new BaseException(NOT_EXIST_EMAIL); //존재하지 않으면 에러코드
        }
        //존재하면 임시 비밀번호 메일 전송
        try{
            //memberId 가져오기
            int memberId = passwordRepository.getMemberId(req.getMemberEmail());
            //임시 비밀번호 만들기
            String randomPw = createRandomPw();
            log.info(randomPw);
            //비밀번호 암호화하고 저장하는 함수
            pwEncrypt(randomPw,memberId);
            log.info("pwEncrypt");
            //메일 보내기
            MailDto mailDto = new MailDto(req.getMemberEmail(), "Mentos 인증번호", randomPw);
            mailService.mailSend(mailDto);
            log.info(mailDto.getMessage());
            return randomPw;
        }catch(Exception e){
            throw new BaseException(MAIL_SEND_ERROR); //메일전송 실패 에러
        }
    }

    @Transactional(rollbackFor = {BaseException.class,Exception.class})
    public void pwEncrypt(String password,int memberId) throws BaseException {
        try {
            String AESPw = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(password);
            log.info(AESPw);
            passwordRepository.setPw(AESPw,memberId); //DB에 저장
        } catch (Exception ignored){ //암호화 실패한 경우
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
    }

    //임시 비밀번호 발급 (소문자, 숫자, 특수문자 포함 10글자)
    public String createRandomPw() {
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '!', '@', '#', '$', '%', '^', '&' };
        StringBuffer sb = new StringBuffer();
        SecureRandom sr = new SecureRandom();
        sr.setSeed(System.currentTimeMillis());
        int idx = 0;
        int len = charSet.length;
        for (int i=0; i<10; i++) {
            idx = sr.nextInt(len);
            // 강력한 난수를 발생시키기 위해 SecureRandom을 사용한다.
            sb.append(charSet[idx]);
        }
            return sb.toString();
    }

    @Override
    public String parseEmail(String email) {
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
    //---------------새 비밀번호 받는 메소드--------------------------

    @Transactional(rollbackFor = {BaseException.class,Exception.class})
    public void changePassword(PostNewPwReq newPwReq, int memberIdByJwt) throws BaseException {
        Member member = passwordRepository.getMember(memberIdByJwt);
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(member.getMemberPw()); // 복호화
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if (newPwReq.getTmpPw().equals(password)) { //임시 비말번호가 일치한다면 새 비밀번호로 바꾼다.
            pwEncrypt(newPwReq.getNewPw(), memberIdByJwt);
        } else { // 비밀번호가 다르다면 에러메세지를 출력한다.
            throw new BaseException(NOT_SAME_PASSWORD);
        }

    }

}
