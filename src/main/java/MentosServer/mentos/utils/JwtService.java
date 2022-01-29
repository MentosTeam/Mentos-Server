package MentosServer.mentos.utils;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.secret.Secret;
import MentosServer.mentos.model.dto.TokenRes;
import MentosServer.mentos.repository.JwtRepository;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static MentosServer.mentos.config.BaseResponseStatus.*;

@Service
public class JwtService {
    private final JwtRepository jwtRepository;

    public JwtService(JwtRepository jwtRepository) {
        this.jwtRepository = jwtRepository;
    }

    /*
    JWT 생성
    @param userIdx
    @return String
     */
    public String createJwt(int memberId){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("memberId",memberId)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*60*24))) //액세스 토큰 만료 시간 하루로 설정 1*(1000*60*60*24) 테스트 1분
                .signWith(SignatureAlgorithm.HS256, Secret.JWT_SECRET_KEY)
                .compact();
    }
    /*
    jwt refreshToken 생성
     */
    public String createRefreshToken(int memberId){
        Date now = new Date();
        String refreshToken = Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*60*24*7))) //리프레시 토큰 만료 시간 일주일로 설정
                .signWith(SignatureAlgorithm.HS256,Secret.JWT_SECRET_KEY)
                .compact();
        jwtRepository.createRefreshToken(memberId,refreshToken);
        return refreshToken;
    }

    /*
    Header에서 X-ACCESS-TOKEN 으로 JWT 추출
    @return String
     */
    public String getJwt(){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("X-ACCESS-TOKEN");
    }
    /*
    Header에서 X-REFRESH-TOKEN으로 REFRESH JWT 추출
     */
    public String getRefreshJwt(){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("X-REFRESH-TOKEN");
    }

    /*
    액세스 토큰이 만료가 되면-> 프론트에서 리프레시 토큰을 보낸다.
    그 리프레시 토큰을 보내면-> 이전 리프레시 토큰과 새로 만든 액세스 토큰을 준다.

    리프레시 토큰도 만료가 됐다면 -> 새로운 리프레시 토큰과 액세스 토큰을 준다.
     */
    @Transactional
    public TokenRes checkRefreshJwt() throws BaseException {

        // Refresh JWT 추출
        int memberId;
        String refreshToken = getRefreshJwt();
        if(refreshToken==null || refreshToken.length()==0){
            throw new BaseException(EMPTY_REFRESH_JWT);
        }
        //memberId 가져오기
        try {
            memberId = jwtRepository.getMemberId(refreshToken); //memberId 얻어오기
        }catch(Exception e){
            throw new BaseException(INVALID_REFRESH_TOKEN);
        }

        Jws<Claims> claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(Secret.JWT_SECRET_KEY)
                    .parseClaimsJws(refreshToken);
        }catch(ExpiredJwtException ignored){ //리프레시 토큰이 만료가 되었으면 새로운 리프레시 토큰과 액세스 토큰을 전달해줌
            String memberJwt = createJwt(memberId); //액세스 토큰을 만든다.
            return new TokenRes(memberJwt,updateRefreshToken(memberId));
        }catch(Exception e){
            throw new BaseException(INVALID_REFRESH_TOKEN);
        }

        return new TokenRes(createJwt(memberId),refreshToken); //액세스 토큰만 만료가 되었을 때 리턴
    }

    public String updateRefreshToken(int memberId) {
        Date now = new Date();
        String refreshToken = Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*60*24*7))) //리프레시 토큰 만료 시간 일주일로 설정
                .signWith(SignatureAlgorithm.HS256,Secret.JWT_SECRET_KEY)
                .compact();
        jwtRepository.updateRefreshToken(memberId,refreshToken);
        return refreshToken;
    }

    /*
    JWT에서 userIdx 추출
    @return int
    @throws BaseException
     */
    public int getMemberId() throws BaseException{
        //1. JWT 추출
        String accessToken = getJwt();
        if(accessToken == null || accessToken.length() == 0){
            throw new BaseException(EMPTY_JWT);
        }

        // 2. JWT parsing
        Jws<Claims> claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(Secret.JWT_SECRET_KEY)
                    .parseClaimsJws(accessToken);
        } catch (ExpiredJwtException ignored) { //액세스 토큰이 만료된 경우
            throw new BaseException(EXPIRED_JWT);
        }catch(Exception e){
            throw new BaseException(INVALID_JWT);
        }

        // 3. userIdx 추출
        return claims.getBody().get("memberId",Integer.class);  // jwt 에서 userIdx를 추출합니다.
    }

    //로그아웃 - 리프레시 토큰 버리기
    public void logOut() throws BaseException {
        int memberId = getMemberId();
        try {
            jwtRepository.deleteRefreshToken(memberId);
        } catch(Exception e){
            if(e instanceof BaseException){
                throw e;
            }
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
