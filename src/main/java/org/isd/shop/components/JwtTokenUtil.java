package org.isd.shop.components;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.isd.shop.entities.Token;
import org.isd.shop.repositories.TokenRepository;
import org.isd.shop.responses.user.RefreshTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    public String generateAccessToken(Long userId, String phoneNumber) {
        //userId Claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId.toString());
        claims.put("phoneNumber", phoneNumber);
        String token = Jwts.builder()
            .setClaims(claims)
            .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration * 1000L))
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact();

        return token;
    }

    public String generateRefreshToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[64]; // 512-bit token
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public Key getSignKey() {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        System.out.println("Key used: " + Base64.getEncoder().encodeToString(key.getEncoded())); // Only for debugging
        return key;
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

    public RefreshTokenResponse generateTokens(Long userId, String username) {
        String accessToken = generateAccessToken(userId, username);
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

    public String extractPhoneNumber(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSignKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("phoneNumber", String.class);
    }

    public String extractClaim(String token, String claim) {
        return (String) Jwts.parserBuilder()
            .setSigningKey(getSignKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get(claim, String.class);
    }
}

