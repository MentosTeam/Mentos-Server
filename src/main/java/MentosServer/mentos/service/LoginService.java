package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.secret.Secret;
import MentosServer.mentos.domain.model.Member;
import MentosServer.mentos.domain.model.PostLoginReq;
import MentosServer.mentos.domain.model.PostLoginRes;
import MentosServer.mentos.repository.LoginRepository;
import MentosServer.mentos.utils.AES128;
import MentosServer.mentos.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static MentosServer.mentos.config.BaseResponseStatus.FAILED_TO_LOGIN;
import static MentosServer.mentos.config.BaseResponseStatus.PASSWORD_DECRYPTION_ERROR;

@Service
public class LoginService {

    private final JwtService jwtService;
    private final LoginRepository loginRepository;



    @Autowired
    public LoginService(LoginRepository loginRepository, JwtService jwtService) {
        this.loginRepository = loginRepository;
        this.jwtService = jwtService;
    }


    // 로그인(password 검사)
    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException {
        Member member = loginRepository.getPwd(postLoginReq);
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(member.getMemberPw()); // 암호화
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if (postLoginReq.getMemberPw().equals(password)) { //비말번호가 일치한다면 memberId를 가져온다.
            int memberId = loginRepository.getPwd(postLoginReq).getMemberId();
            String jwt = jwtService.createJwt(memberId);
            return new PostLoginRes(memberId,jwt);


        } else { // 비밀번호가 다르다면 에러메세지를 출력한다.
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }
}
