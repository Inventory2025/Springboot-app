package com.ims.inventory.security;

import com.ims.inventory.constants.ImsConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);

            if (!username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    JwtUser user = (JwtUser) userDetails;
                    try {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                        String token = jwtUtil.refreshToken(jwt, user);
                        request.setAttribute(ImsConstants.USER_ID, user.getId());
                        request.setAttribute(ImsConstants.ROLE_ID, user.getRoleId());
                        request.setAttribute(ImsConstants.IS_ADMIN_ROLE, ImsConstants.ADMIN_ROLE_ID.equalsIgnoreCase(user.getRoleId()));
                        request.setAttribute(ImsConstants.BRANCH_ID, user.getBranchId());
                        response.addHeader(ImsConstants.TKN, token);
                    } catch (Exception e) {
                        response.setStatus(ImsConstants.SC_UNAUTHORIZED);
                        return;
                    }
                } else {
                    response.setStatus(ImsConstants.SC_UNAUTHORIZED);
                    return;
                }
            } else {
                response.setStatus(ImsConstants.SC_UNAUTHORIZED);
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
