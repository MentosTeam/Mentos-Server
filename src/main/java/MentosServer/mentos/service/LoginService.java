package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.secret.Secret;
import MentosServer.mentos.model.domain.Member;
import MentosServer.mentos.model.dto.PostLoginReq;
import MentosServer.mentos.model.dto.PostLoginRes;
import MentosServer.mentos.repository.LoginRepository;
import MentosServer.mentos.utils.AES128;
import MentosServer.mentos.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static MentosServer.mentos.config.BaseResponseStatus.*;

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
        if(loginRepository.checkEmail(postLoginReq.getMemberEmail())==0){
            throw new BaseException(FAILED_TO_LOGIN);
        }
        Member member = loginRepository.getPwd(postLoginReq);
        String password;


        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(member.getMemberPw()); // 암호화
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(!member.getMemberStatus().equals("active")){
            throw new BaseException(FAILED_TO_LOGIN_UNACTIVE);
        }

        if (postLoginReq.getMemberPw().equals(password)) { //비말번호가 일치한다면 memberId를 가져온다.
            int memberId = loginRepository.getPwd(postLoginReq).getMemberId();

            String jwt = jwtService.createJwt(memberId);
            int mentor = loginRepository.checkMentor(memberId);
            int mentee = loginRepository.checkMentee(memberId);
            int[] flag = new int[2];
            flag = loginRepository.checkMemberFlag(memberId);
            String mentorImage;
            String menteeImage;
            if( mentor ==0){
                mentorImage = "";
            }
            else{
                mentorImage = loginRepository.getMentorImage(memberId);
            }
            if (mentee ==0){
                menteeImage = "";
            }
            else{
                menteeImage = loginRepository.getMenteeImage(memberId);
            }
            //멘토,멘티 프로필 유무 반환
            //존재하면1, 없으면0
            return new PostLoginRes(memberId, jwt, mentor, mentee, member.getMemberNickName(), mentorImage, menteeImage,flag[0],flag[1]);


        } else { // 비밀번호가 다르다면 에러메세지를 출력한다.
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }
}
