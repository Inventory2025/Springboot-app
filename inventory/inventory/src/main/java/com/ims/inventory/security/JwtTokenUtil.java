package com.ims.inventory.security;

import com.ims.inventory.utility.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Autowired
    private JwtUtil jwtUtil;

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return jwtUtil.createToken(claims, userDetails.getUsername());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = jwtUtil.extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !jwtUtil.isTokenExpired(token));
        } catch (Exception e) {
            log.info("JwtTokenUtil::validateToken()-->",e);
            return Boolean.FALSE;
        }

    }

    public String refreshToken(String token, JwtUser user) {
        return jwtUtil.refreshToken(token, user.getUsername(), user.getExpTime());
    }

    public String extractUsername(String token) {
        try {
            return jwtUtil.extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            log.info("JwtTokenUtil::extractUsername()-->",e);
            return StringUtils.EMPTY;
        }
    }

}
