package com.ims.inventory.security;

import com.ims.inventory.utility.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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
        final String username = jwtUtil.extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !jwtUtil.isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return jwtUtil.extractClaim(token, Claims::getSubject);
    }

}
