package MentosServer.mentos.controller;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponse;
import MentosServer.mentos.config.BaseResponseStatus;
import MentosServer.mentos.model.dto.PatchPostsReq;
import MentosServer.mentos.model.dto.PostPostsReq;
import MentosServer.mentos.model.dto.PostPostsRes;
import MentosServer.mentos.service.BoardService;
import MentosServer.mentos.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static MentosServer.mentos.config.BaseResponseStatus.*;

@RestController
public class BoardController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final BoardService boardService;
    private final JwtService jwtService;

    @Autowired
    public BoardController(BoardService boardService, JwtService jwtService) {
        this.boardService = boardService;
        this.jwtService = jwtService;
    }

    /**
     *
     * 멘토쓰 찾기 게시글 등록
     * 임시로 글만 적는 것만 구현
     *
     * jwt 확인 -> 멘토인지 확인 -> 글 등록
     *
     * @return
     */
    @PostMapping("/posts")
    public BaseResponse<PostPostsRes> createPost(@Valid @ModelAttribute("postReq") PostPostsReq postPostsReq, BindingResult br) throws BaseException {
        //validation 추가
        if(br.hasErrors()){
            String errorName = br.getAllErrors().get(0).getDefaultMessage();
            return new BaseResponse<>(BaseResponseStatus.of(errorName));
        }
        //글 작성
        try{
            int memberIdByJwt = jwtService.getMemberId();
            PostPostsRes postPostsRes = boardService.createPost(memberIdByJwt,postPostsReq);
            return new BaseResponse<>(postPostsRes);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 게시글 수정
     * requestBody는 게시글 등록과 동일
     * 글 내용 전체 수정 -> updateAt 시간 변경
     * 성공하면 성공코드 전달
     *
     * @param postId
     * @return
     *
     */
    @PatchMapping("/posts/{postId}")
    public BaseResponse modifyPost(@PathVariable int postId, @Valid @ModelAttribute("patchReq")PatchPostsReq patchPostsReq){
        try{
            int memberIdByJwt = jwtService.getMemberId();
            boardService.modifyPost(memberIdByJwt,postId,patchPostsReq);
            return new BaseResponse<>(PATCH_POST_SUCCESS);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 데이터 아예 삭제
     */
    @DeleteMapping("/posts/{postId}")
    public BaseResponse deletePost(@PathVariable int postId) throws BaseException {
        //글 삭제
        try{
            int memberIdByJwt = jwtService.getMemberId();
            boardService.deletePost(memberIdByJwt,postId);
            return new BaseResponse<>(DELETE_POST_SUCCESS);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
