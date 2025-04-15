package com.ims.inventory.controller;

import com.ims.inventory.domen.request.AuthRequest;
import com.ims.inventory.domen.response.AuthenticationResponse;
import com.ims.inventory.security.JwtTokenUtil;
import com.ims.inventory.service.UserMasterService;
import com.ims.inventory.service.impl.UserDetailsServiceImpl;
import com.ims.inventory.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
    @RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMasterService userMasterService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody AuthRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        AuthenticationResponse resp = new AuthenticationResponse();
        resp.setTkn(jwt);
        resp.setUsername(userDetails.getUsername());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/test/{pass}")
    public ResponseEntity<?> getPassword(@PathVariable("pass") String pass) {
        String passwordEncoderStr = passwordEncoder.encode(pass);
        log.info("getPassword ---> Pass :{}, Encode :{}", pass, passwordEncoderStr);
        return ResponseEntity.ok(passwordEncoderStr);
    }

}
