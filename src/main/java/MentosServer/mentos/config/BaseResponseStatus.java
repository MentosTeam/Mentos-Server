package MentosServer.mentos.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {

    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),
    INVALID_ACCESS(false, 2004, "잘못된 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),
    
    // [GET] /schoolCertification
    GET_USERS_EMPTY_EMAIL(false, 2019, "이메일을 입력해주세요."),
    GET_USERS_INVALID_EMAIL(false, 2020, "이메일 형식을 확인해주세요."),
    GET_USERS_EXISTS_EMAIL(false,2021,"중복된 이메일입니다."),
    INVALID_SCHOOL_EMAIL(false, 2022, "학교 이메일이 아닙니다."),
    
    // Item
    ITEM_EMPTY_ITEM_ID(false, 2018, "아이템 아이디 값을 확인해주세요."),

    /**
     * ROZY
     * 201 - 300 : Requset 오류
     */
    //[POST] members/setProfile
    POST_PROFILE_INVALID_ROLE(false, 2201, "올바르지 않은 역할(멘토/멘티) 선택입니다."),
    POST_PROFILE_EMPTY_MEMBERID(false, 2202, "memberId가 입력되지 않았습니다."),
    POST_PROFILE_EMPTY_MAJORFIRST(false, 2203, "majorFirst가 선택되지 않았습니다."),
    POST_PROFILE_EMPTY_INTRODUCTION(false, 2204, "자기소개가 입력되지 않았습니다."),
    POST_PROFILE_SHORT_INTRODUCTION(false, 2205, "자기소개는 10글자 이상 입력해주세요."),
    POST_PROFILE_INVALID_IMAGEURL(false, 2206, "이미지 확장자(jpg | jpeg | png | bmp)를 확인해주세요."),

    //[POST] /mentoring/registration
    POST_MENTORING_INVALID_MENTOID(false, 2208, "유효하지 않은 mentoId 값입니다."),
    POST_MENTORING_GERATER_MENTORINGCOUNT(false, 2209, "멘토링 횟수(mentoringCount)는 최대 10입니다."),
    POST_MENTORING_LESS_MENTORINGCOUNT(false, 2210, "멘토링 횟수(mentoringCount)는 최소 1입니다."),
    POST_MENTORING_INVALID_MAJORCATEGORYID(false, 2211, "유효하지 않은 멘토-쓰 종류(majorCategoryId)값 입니다."),
    POST_MENTORING_INVALID_MENTOS(false, 2212, "멘토-쓰 개수(mentos)의 형식을 확인해주세요."),
    POST_MENTORING_SAME_MENTOMENTI(false, 2213, "멘토(mentoId)와 멘티(mentiId)가 같습니다."),

    //[POST] /mentoring/acceptance
    POST_INVALID_MENTORING(false,2214, "유효하지 않은 멘토링 요청입니다."),

    //[PATCH] /mentoring/stop
    PATCH_INVALID_MENTORING(false,2215, "유효하지 않은 멘토링입니다."),

    //[POST] /mentoring/review
    POST_REVIEW_EXISTS(false,2220,"리뷰가 이미 등록되어있습니다."),


    // [POST] /sign-up
    EMPTY_USER_NAME(false,2401,"이름을 입력해주세요"),
    INVALID_USER_NAME(false,2402,"이름의 형식을 확인해주세요"),
    EMPTY_USER_NICKNAME(false,2403,"닉네임을 입력해주세요"),
    INVALID_USER_NICKNAME(false,2404,"닉네임의 형식을 확인해주세요"),
    EMPTY_USER_SEX(false,2405,"성별을 입력해주세요"),
    EMPTY_USER_STUDENT_ID(false,2406,"학번을 입력해주세요"),
    EMPTY_USER_SCHOOL_NAME(false,2407,"학교명을 입력해주세요"),
    EMPTY_USER_PASSWORD(false,2408,"비밀번호를 입력해주세요"),
    INVALID_USER_PASSWORD(false,2409,"비밀번호의 형식을 확인해주세요"),

    // [POST]/board
    EMPTY_MAJOR_CATEGORY(false,2410,"게시글 전공 카테고리를 선택해주세요"),
    EMPTY_POST_TITLE(false,2411,"게시글 제목을 입력해주세요"),
    EMPTY_POST_CONTENTS(false,2412,"게시글 내용을 입력해주세요"),

    //[POST]/pwChange
    EMPTY_TMP_PASSWORD(false,2413,"임시 비밀번호를 입력해주세요"),



    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),
    FAILED_TO_LOGIN_UNACTIVE(false, 3015, "탈퇴한 회원입니다."),
    
    // [POST] /items

    DUPLICATED_NICKNAME(false, 3401, "중복된 닉네임입니다."),
    VALID_USER_NICKNAME(true,3402,"사용가능한 닉네임입니다."),
    //[DELETE]/board
    DELETE_POST_SUCCESS(true,3403,"게시글 삭제를 완료했습니다."),
    INVALID_POST_MEMBER(false,3404,"게시글 작성자가 아닙니다"),
    //[PATCH]/board
    PATCH_POST_SUCCESS(true,3405,"게시글 수정을 완료했습니다."),
    //[POST]/password
    SUCESS_SEND_PASSWORD(true,3406,"임시 비밀번호 전송에 성공했습니다."),
    NOT_EXIST_EMAIL(false,3407,"존재하지 않는 이메일입니다."),
    //[POST]/pwChange
    NOT_SAME_PASSWORD(false, 3408,"현재 비밀번호가 같지 않습니다."),
    SUCESS_CHANGE_PASSWORD(true,3409,"비밀번호 변경에 성공했습니다"),
    /**
     * ROZY
     * 301 -  400 : Response 오류
     */

    //[POST] /mentoring/registration
    POST_MENTORING_DUPLICATED_MENTORING(false,3302, "해당 멘토에게 수락 요청 대기 중인 멘토링 요청이 있습니다."),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),


    //S3 Delete 오류
    S3_DELETE_ERROR(false,4401,"S3 이미지 삭제에 실패하였습니다."),

    /**
     * ROZY
     * 301 -  400 : Response 오류
     */
    FAILED_TO_SETPROFILE(false,4301,"프로필 등록에 실패했습니다."),
    POST_DUPLICATED_PROFILE(false,4302,"멘티, 멘토 프로필이 모두 존재합니다."),

    //[POST] /mentoring/acceptance
    FAILED_TO_ACCEPTMENTORING(false,4303,"멘토링 요청 수락에 실패하였습니다."),
    FAILED_TO_REJECTMENTORING(false,4304,"멘토링 요청 거절에 실패하였습니다."),

    //[PATCH] /mentoring/stop
    FAILED_TO_STOPMENTORING(false,4305,"멘토링 강제 종료에 실패하였습니다."),

    //[DELETE] /mentoring/cancel
    FAILDE_TO_DELETEMENTORING(false,4306,"멘토링 요청 취소에 실패하였습니다."),

    // [GET] /schoolCertification
    MAIL_SEND_ERROR(false, 4015, "메일 전송에 실패하였습니다.");

    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
    public static BaseResponseStatus of(final String errorName){
        return BaseResponseStatus.valueOf(errorName);
    }
}
