package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;

import java.util.List;

public interface FcmTokenService{

    int insertNewUserDeviceToken(int memberId, String fcmToken) throws BaseException;
    List<String> selectUserDeviceTokenByIdx(int memberId) throws BaseException;
    int deleteUserDeviceToken(int memberId, String fcmToken)throws BaseException;
    int updateUserDeviceToken(int memberId, String oldFcmToken, String newFcmToken)throws BaseException;
    int deleteUserAllDeviceToken(int memberId) throws BaseException;
}
