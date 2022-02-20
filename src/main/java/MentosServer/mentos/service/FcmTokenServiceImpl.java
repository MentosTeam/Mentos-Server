package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.repository.FcmTokenRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

import static MentosServer.mentos.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class FcmTokenServiceImpl implements FcmTokenService {
    private final FcmTokenRespository fcmTokenRespository;

    @Autowired
    public FcmTokenServiceImpl(FcmTokenRespository fcmTokenRespository) {
        this.fcmTokenRespository = fcmTokenRespository;
    }

    @Override
    public int insertNewUserDeviceToken(int memberId, String fcmToken) throws BaseException {
        try{
            if(fcmTokenRespository.existDeviceToken(memberId,fcmToken)==1){ //이미 있으면
                return 0;
            }
            return fcmTokenRespository.insertNewUserDeviceToken(memberId,fcmToken);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Override
    public List<String> selectUserDeviceTokenByIdx(int memberId)throws BaseException {
        try{
            return fcmTokenRespository.selectUserDeviceTokenByIdx(memberId);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Override
    public int deleteUserDeviceToken(int memberId, String fcmToken)throws BaseException {
        try{
            return fcmTokenRespository.deleteUserDeviceToken(memberId,fcmToken);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Override
    public int updateUserDeviceToken(int memberId, String oldFcmToken, String newFcmToken)throws BaseException {
        try{
            return fcmTokenRespository.updateUserDeviceToken(memberId,oldFcmToken,newFcmToken);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Override
    public int deleteUserAllDeviceToken(int memberId) throws BaseException {
        try{
            return fcmTokenRespository.deleteUserAllDeviceToken(memberId);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
