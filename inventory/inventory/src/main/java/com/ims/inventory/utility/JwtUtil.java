package com.ims.inventory.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.apache.bcel.ExceptionConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtUtil {

    private String SECRET_KEY = "5367566B59703373367639792F423F4528482B4D6251655468576D5A54347437";

    @Value("jwr.secret.key")
    String custom_key;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey getSecretKey() {
        custom_key = "$2a$10$Ts.9JEroYmbEq4CDo1ahyOZBa80HcIoKTbITLGRRpFzwKIF.yjZ.W";
        // Encode the custom key to Base64
        String BASE64_SECRET_KEY = Base64.getEncoder().encodeToString(custom_key.getBytes(StandardCharsets.UTF_8));
        // Decode the Base64 key to use with JWT
        SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(BASE64_SECRET_KEY));
        return SECRET_KEY;
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String createToken(Map<String, Object> claims, String subject) {
        return buildToken(claims, subject, expiration); //SignatureAlgorithm.HS256,
    }

    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }

    public String refreshToken(String token, String subject, long expTime) {
        final Claims claims = extractAllClaims(token);
        return buildToken(claims, subject, expTime);
    }

    private String buildToken(Map<String, Object> claims, String subject, long expTime) {
        try {
            return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                    // .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60))
                    .setExpiration(new Date(System.currentTimeMillis() + (1000 * expTime)))
                    .signWith(getSignKey(), SignatureAlgorithm.HS256).compact(); //SignatureAlgorithm.HS256,
        } catch (Exception e) {
           log.error("JwtUtil::buildToken::Exception occurred :",e);
           return StringUtils.EMPTY;
        }
    }
}
