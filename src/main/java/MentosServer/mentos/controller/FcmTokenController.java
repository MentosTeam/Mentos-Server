package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.model.dto.DeviceTokenDto;
import MentosServer.mentos.service.FcmTokenService;
import MentosServer.mentos.service.FcmTokenServiceImpl;
import MentosServer.mentos.utils.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static MentosServer.mentos.config.BaseResponseStatus.SUCCESS;

@Slf4j
@RestController
public class FcmTokenController {
    private final FcmTokenService fcmTokenService;
    private final JwtService jwtService;

    @Autowired
    public FcmTokenController(FcmTokenServiceImpl fcmTokenService, JwtService jwtService) {
        this.fcmTokenService = fcmTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/device")
    public BaseResponse updateDeviceToken(@RequestBody DeviceTokenDto deviceTokenDto) throws BaseException {
        int memberIdByJwt = jwtService.getMemberId();
        try {
            //디바이스 등록
            if (deviceTokenDto.getNewToken().equals(deviceTokenDto.getCurrToken())) {
                log.info("등록");
                fcmTokenService.insertNewUserDeviceToken(memberIdByJwt, deviceTokenDto.getCurrToken());
            } else {
                log.info("변경");
                fcmTokenService.updateUserDeviceToken(memberIdByJwt, deviceTokenDto.getCurrToken(), deviceTokenDto.getNewToken());
            }
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //로그아웃 시에 삭제
    @DeleteMapping("/device")
    public BaseResponse deleteDeviceToken(@RequestBody DeviceTokenDto deviceTokenDto) throws BaseException {
        int memberIdByJwt = jwtService.getMemberId();
        try{
            fcmTokenService.deleteUserDeviceToken(memberIdByJwt, deviceTokenDto.getCurrToken());
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
