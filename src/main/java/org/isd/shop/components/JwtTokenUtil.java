package org.isd.shop.components;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.isd.shop.entities.Token;
import org.isd.shop.entities.User;
import org.isd.shop.repositories.TokenRepository;
import org.isd.shop.responses.user.RefreshTokenResponse;
import org.isd.shop.responses.user.UserLoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Getter
@Setter
public class JwtTokenUtil {
    @Value("${jwt.accessTokenExpiration}")
    private int accessTokenExpiration;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.refreshTokenExpiration}")
    private int refreshTokenExpiration;

    private final TokenRepository tokenRepository;

    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration * 1000L))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[64]; // 512-bit token
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public Key getSignKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isValidAccessToken(String token, String username) {

        return extractUsername(token).equals(username) && !isAccessTokenExpired(token);
    }

    public boolean isAccessTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    public boolean isRefreshTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

    public RefreshTokenResponse generateTokens(String username) {
        String accessToken = generateAccessToken(username);
        String refreshTokenString = generateRefreshToken();
        Date refreshTokenExpiration = new Date(System.currentTimeMillis() + getRefreshTokenExpiration() * 1000L);
        Date accessTokenExpiration = new Date(System.currentTimeMillis() + getAccessTokenExpiration() * 1000L);
        Token token = Token.builder()
                .refreshToken(refreshTokenString)
                .accessToken(accessToken)
                .refreshTokenExpiryDate(refreshTokenExpiration)
                .accessTokenExpiryDate(accessTokenExpiration)
                .build();
        tokenRepository.save(token);
        return RefreshTokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshTokenString).build();
    }

}

