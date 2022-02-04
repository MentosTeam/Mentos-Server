package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.model.dto.PatchMemberLeaveReq;
import MentosServer.mentos.service.MemberLeaveService;
import MentosServer.mentos.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/setting")
public class MemberLeaveController {
    private final MemberLeaveService memberLeaveService;
    private final JwtService jwtService;

    @Autowired
    public MemberLeaveController(MemberLeaveService memberLeaveService, JwtService jwtService){
        this.memberLeaveService = memberLeaveService;
        this.jwtService = jwtService;
    }

    /**
     * 회원 탈퇴 API
     * @param patchMemberLeaveReq
     * @return String
     */
    @PatchMapping("/member-leave")
    public BaseResponse<String> deleteMember(@RequestBody PatchMemberLeaveReq patchMemberLeaveReq){
        try{
            int memberIdByJwt = jwtService.getMemberId();
            memberLeaveService.deleteMember(memberIdByJwt, patchMemberLeaveReq);
            return new BaseResponse<>("성공적으로 회원탈퇴 되었습니다.");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
