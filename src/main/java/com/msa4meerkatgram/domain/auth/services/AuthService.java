package com.msa4meerkatgram.domain.auth.services;

import com.msa4meerkatgram.domain.auth.mapper.AuthMapper;
import com.msa4meerkatgram.domain.auth.requests.LoginReq;
import com.msa4meerkatgram.domain.auth.responses.AuthRes;
import com.msa4meerkatgram.domain.user.entities.User;
import com.msa4meerkatgram.domain.user.mapper.UserMapper;
import com.msa4meerkatgram.domain.user.responses.UserRes;
import com.msa4meerkatgram.global.errors.custom.InvalidTokenException;
import com.msa4meerkatgram.global.errors.custom.NotRegisteredException;
import com.msa4meerkatgram.global.security.cookie.CookieManager;
import com.msa4meerkatgram.global.security.jwt.JwtConfig;
import com.msa4meerkatgram.global.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;
    private final AuthMapper authMapper;
    private final CookieManager cookieManager;
    private final JwtConfig jwtConfig;

    public AuthRes login(HttpServletResponse response, LoginReq loginReq) {
    // 유저정보 획득
    User user = userMapper.findByEmail(loginReq.email());

    // 유저 가입 여부 확인
    if(user == null) {
        throw new NotRegisteredException("아이디와 비밀번호를 확인해주세요.");
    }

    // 비밀번호 체크
        return this.generateAuthentication(response, user);
    }


    public AuthRes reissue(HttpServletRequest request, HttpServletResponse response) {
        // 리프래시 토큰 획득
        Optional<String> refreshTokenOptional = jwtProvider.extractRefreshToken(request);
        if(refreshTokenOptional.isEmpty()) {
            throw new InvalidTokenException("토큰이 없습니다.");
        }
        String extractRefreshToken = refreshTokenOptional.get();

        long id = Long.parseLong(jwtProvider.extractClaims(extractRefreshToken).getSubject());

        // 유저 획득
        User user = userMapper.findByPk(id);     // 질문 : User.java에서 바로 가져오지 않고 Mapper를 따로 만들어 두번 나눠 가져오는 이유가 무엇입니까?
                                                 // 답변 : 파라미터만 전달해 주어서 딱히 id가 아니어도 관계없음(User.java에 있는 거 아님! 위의 long id임)
        // 유저 가입 여부 확인 및 비로그인 상태 확인
        if(user == null || user.getRefreshToken() == null) {
            throw new InvalidTokenException("유효하지 않은 회원의 토큰입니다.");
        }

        // 리프래시 토큰 비교(DB에 저장되어 있는 것과 요청 쿠키에 저장된 것끼리)
        if(!user.getRefreshToken().equals(extractRefreshToken)) {  // 참조 타입이 아닌 객체 타입이기 때문에 '=='는 금지!
            // 질문 : getRefreshToken()는 여기 어디에서도 발견이 되지 않는데 어떻게 된 것입니까?
            // 답변 : domain.user.entities.User.java에 @Getter, @Setter 어노테이션이 있기 때문에 이미 자동생성됨.
            throw new InvalidTokenException("토큰이 일치하지 않습니다.");
        }

        return this.generateAuthentication(response, user);
    }


    /**
     * 엑세스토큰 및 리프래시토큰 생성 후, 리프래시 토큰 DB&Cookie에 저장, AuthRes로 반환
     * @param response
     * @param user  유저  Entity
     * @return AuthRes
     */
    private AuthRes generateAuthentication(HttpServletResponse response, User user) {
        // 토큰 생성
        String newAccessToken = jwtProvider.generateAccessToken(user);
        String newRefreshToken = jwtProvider.generateRefreshToken(user);

        // 리프래시 토큰을 DB 저장
        authMapper.updateRefreshToken(user.getId(), newRefreshToken);

        // 리프래시 토큰을 Cookie에 저장
        cookieManager.setCookie(response, jwtConfig.refreshTokenCookieName(), newRefreshToken, jwtConfig.refreshTokenCookieExpiry(), jwtConfig.reissUri());

        // 리턴
        return AuthRes.builder()
                .accessToken(newAccessToken)
                .user(
                        UserRes.builder()
                                .email(user.getEmail())
                                .nick(user.getNick())
                                .role(user.getRole())
                                .profile(user.getProfile())
                                .createdAt(user.getCreatedAt())
                                .build()
                )
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public void logout(HttpServletResponse response, long id) {

        // 유저 정보 획득
        User user = userMapper.findByPk(id);

        if (user == null)
            throw new InvalidTokenException("유효하지 않은 회원의 토큰입니다.");

        // DB에 저장된 refresh 토큰 파기
        authMapper.updateRefreshToken(user.getId(), null);

        // Cookie에 저장된 refresh 토큰 파기
        cookieManager.setCookie(
                response
                , jwtConfig.refreshTokenCookieName()
                , null
                , 0
                , jwtConfig.reissUri()
        );
    }
}
