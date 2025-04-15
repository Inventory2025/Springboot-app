package com.ims.inventory.controller;

import com.ims.inventory.domen.request.FindUserRequest;
import com.ims.inventory.domen.request.SignUpRequest;
import com.ims.inventory.service.UserMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserMasterService userMasterService;

    @PostMapping("find/all")
    public ResponseEntity<?> findAll(
            @RequestBody FindUserRequest findUserRequest) throws Exception {
       return ResponseEntity.ok(userMasterService.findAllUserByIsActive(findUserRequest.isActive()));
    }

    @PostMapping("signup")
    public ResponseEntity<?> addUser(
            @RequestBody SignUpRequest signUpRequest) throws Exception {
        try {
            return ResponseEntity.ok(userMasterService.addUser(signUpRequest));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
    }
}
