package com.msa4meerkatgram.global.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")  // 환경설정파일(application.yaml)에 있는 값을 가져온다.

public record JwtConfig(
        boolean secure,
        String issuer,
        String type,
        int accessTokenExpiry,
        int refreshTokenExpiry,
        String refreshTokenCookieName,
        int refreshTokenCookieExpiry,
        String secret,
        String headerKey,
        String scheme,
        String reissUri
) {
}
