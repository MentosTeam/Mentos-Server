package MentosServer.mentos.service;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.model.domain.Post;
import MentosServer.mentos.model.domain.PostImage;
import MentosServer.mentos.model.dto.PatchPostsReq;
import MentosServer.mentos.model.dto.PostPostsReq;
import MentosServer.mentos.model.dto.PostPostsRes;
import MentosServer.mentos.repository.BoardRepository;
import MentosServer.mentos.utils.FileUploadService;
import MentosServer.mentos.utils.JwtService;
import io.jsonwebtoken.lang.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static MentosServer.mentos.config.BaseResponseStatus.DATABASE_ERROR;
import static MentosServer.mentos.config.BaseResponseStatus.INVALID_POST_MEMBER;

@Service
public class BoardService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final BoardRepository boardRepository;
    private final JwtService jwtService;
    private final FileUploadService fileUploadService;

    @Autowired
    public BoardService(BoardRepository boardRepository, JwtService jwtService, FileUploadService fileUploadService) {
        this.boardRepository = boardRepository;
        this.jwtService = jwtService;
        this.fileUploadService = fileUploadService;
    }


    /**
     * 게시글 작성
     * 1. 이미지 업로드 method 호출
     * 2. 리턴 값을 imgUrlList에 삽입
     * 3. post객체에 넣어주기
     * 4. createPost method 호출
     * @param memberIdByJwt
     * @param postPostsReq
     * @return
     * @throws BaseException
     */
    @Transactional(rollbackFor = {Exception.class,BaseException.class})
    public PostPostsRes createPost(int memberIdByJwt, PostPostsReq postPostsReq) throws BaseException {
        Post post = new Post(postPostsReq.getMajorCategoryId(),memberIdByJwt,
                postPostsReq.getPostTitle(),postPostsReq.getPostContents());;
        try{
            if(postPostsReq.getImageFile()==null){ //이미지 첨부 안한 경우
                return new PostPostsRes(boardRepository.createPost(post));
            }
            int postId = boardRepository.createPost(post); //글을 먼저 입력
            PostImage postImage = imgToUrl(postId,postPostsReq.getImageFile());//이미지 S3에 업로드
            boardRepository.uploadPostImage(postImage); //url 테이블에 삽입
            return new PostPostsRes(postId);
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 이미지 S3에 업로드 후 url 리스트를 반환
     * @param postId
     * @param imgFile
     * @return
     */
    public PostImage imgToUrl(int postId, MultipartFile imgFile) throws BaseException {
        String imageUrl = fileUploadService.uploadS3Image(imgFile);

        return new PostImage(postId,imageUrl);
    }

    /**
     * 게시글 수정
     * @param memberIdByJwt
     * @param postId
     * @param patchPostsReq
     * @throws BaseException
     */
    @Transactional(rollbackFor = {Exception.class,BaseException.class})
    public void modifyPost(int memberIdByJwt, int postId, PatchPostsReq patchPostsReq) throws BaseException {
        Post post = new Post(postId,patchPostsReq.getMajorCategoryId(),memberIdByJwt,
                patchPostsReq.getPostTitle(),patchPostsReq.getPostContents());
        try{
            //postId와 memberId 검증
            if(checkPostMember(memberIdByJwt,postId)==false){
                throw new BaseException(INVALID_POST_MEMBER);
            }
            //patch - 글 수정
            boardRepository.patchPost(post);
            //Image 없으면 -> 이미지 삭제 여부 확인
            if(patchPostsReq.getImageFile()==null || patchPostsReq.getImageFile().isEmpty()){
                if(patchPostsReq.getImageUrl()==null||patchPostsReq.getImageUrl().isEmpty()) { //이미지 삭제를 했다면?
                    //기존 이미지 있었는지 확인하고 삭제
                    Optional<String> imageUrl = boardRepository.getImageUrl(postId);
                    if(imageUrl.isPresent()) { //
                        fileUploadService.deleteS3Image(imageUrl.get());
                        boardRepository.deletePostImage(postId);
                    }
                }

            }
            //요청 이미지 파일 있다면? -> 교체
            else{
                //이미지 이름 가져와서 삭제
                Optional<String> imageUrl = boardRepository.getImageUrl(postId);
                if(imageUrl.isPresent()) {
                    fileUploadService.deleteS3Image(imageUrl.get());
                    boardRepository.deletePostImage(postId);
                }
                //새로운 이미지 업로드
                PostImage postImage = imgToUrl(postId, patchPostsReq.getImageFile());
                boardRepository.uploadPostImage(postImage);
            }

        }catch(BaseException exception){
            throw new BaseException(exception.getStatus());
        }
    }


    /**
     * 게시글 삭제
     * -> S3에도 이미지 삭제 필요
     * @param memberIdByJwt
     * @param postId
     * @throws BaseException
     */
    @Transactional(rollbackFor = {Exception.class,BaseException.class})
    public void deletePost(int memberIdByJwt, int postId) throws BaseException {
        try{
            //postId와 memberId 검증
            if(checkPostMember(memberIdByJwt,postId)==false){
                throw new BaseException(INVALID_POST_MEMBER);
            }
            //delete postId
            boardRepository.deletePost(postId);
            //s3이미지 삭제
            Optional<String> imageUrl = boardRepository.getImageUrl(postId);
            if(imageUrl.isPresent()) {
                fileUploadService.deleteS3Image(imageUrl.get()); //S3에 게시글 이미지 삭제
                boardRepository.deletePostImage(postId);//DB에 튜플 삭제
            }
        }catch(BaseException exception){
            throw new BaseException(exception.getStatus());
        }
    }

    /**
     * 게시글 작성자가 맞는지 확인
     * @param memberIdByJwt
     * @param postId
     * @return
     * @throws BaseException
     */
    public boolean checkPostMember(int memberIdByJwt, int postId) throws BaseException {
        try{
            if (boardRepository.checkPostMember(postId) != memberIdByJwt) {
                return false;
            }
            return true;
        }catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
