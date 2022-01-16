package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.secret.Secret;
import MentosServer.mentos.model.dto.NickNameChkReq;
import MentosServer.mentos.model.dto.NickNameChkRes;
import MentosServer.mentos.model.dto.SignUpReq;
import MentosServer.mentos.model.dto.SignUpRes;
import MentosServer.mentos.repository.SignUpRepository;
import MentosServer.mentos.utils.AES128;
import MentosServer.mentos.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static MentosServer.mentos.config.BaseResponseStatus.DATABASE_ERROR;
import static MentosServer.mentos.config.BaseResponseStatus.PASSWORD_ENCRYPTION_ERROR;

@Service
public class SignUpService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final SignUpRepository signUpRepository;
    private final JwtService jwtService;

    public SignUpService(SignUpRepository signUpRepository, JwtService jwtService) {
        this.signUpRepository = signUpRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public SignUpRes createMember(SignUpReq signUpReq) throws BaseException {
        //중복 확인 (닉네임, 이메일) 
        //비밀번호 암호화
        String pwd;
        try {
            // 암호화: signUpReq에서 제공받은 비밀번호를 보안을 위해 암호화시켜 DB에 저장합니다.
            // ex) password123 -> dfhsjfkjdsnj4@!$!@chdsnjfwkenjfnsjfnjsd.fdsfaifsadjfjaf
            logger.info("암호화 로직");
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(signUpReq.getMemberPw()); // 암호화코드
            signUpReq.setMemberPw(pwd);
        } catch (Exception ignored) { // 암호화가 실패하였을 경우 에러 발생
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        //실제 비즈니스 로직
        try {
            int memberId = signUpRepository.createMember(signUpReq);
            logger.info("jwt발급");
            String memberJwt = jwtService.createJwt(memberId);
            return new SignUpRes(memberId,memberJwt);
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public NickNameChkRes checkNickName(NickNameChkReq nickChkReq) throws BaseException {
        try {
            if(signUpRepository.checkNickName(nickChkReq)==1){
                return new NickNameChkRes(true);
            }else{
                return new NickNameChkRes(false);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
