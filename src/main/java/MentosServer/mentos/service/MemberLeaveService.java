package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.secret.Secret;
import MentosServer.mentos.model.dto.PatchMemberLeaveReq;
import MentosServer.mentos.repository.MemberLeaveRepository;
import MentosServer.mentos.utils.AES128;
import MentosServer.mentos.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static MentosServer.mentos.config.BaseResponseStatus.*;

@Service
public class MemberLeaveService {
    private final MemberLeaveRepository memberLeaveRepository;
    private final FcmTokenService fcmTokenService;
    private final JwtService jwtService;

    @Autowired
    public MemberLeaveService(MemberLeaveRepository memberLeaveRepository, FcmTokenService fcmTokenService, JwtService jwtService){
        this.memberLeaveRepository = memberLeaveRepository;
        this.fcmTokenService = fcmTokenService;
        this.jwtService = jwtService;
    }

    //회원탈퇴
    public void deleteMember(int memberId, PatchMemberLeaveReq patchMemberLeaveReq) throws BaseException {
        String pwd;

        try{
            pwd = memberLeaveRepository.getPassword(memberId);
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }

        try {
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(pwd);
        } catch (Exception e) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(patchMemberLeaveReq.getPassword().equals(pwd)){
            fcmTokenService.deleteUserAllDeviceToken(memberId); //모든 디바이스 토큰 삭제
            if(memberLeaveRepository.modifyMemberStatus(memberId) == 0){
                throw new BaseException(FAILED_TO_MEMBERLEAVE);
            }
        }
        else {
            throw new BaseException(INVALID_PASSWORD);
        }
    }
}
