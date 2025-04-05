package com.ims.inventory.controller;

import com.ims.inventory.domen.request.FindUserRequest;
import com.ims.inventory.domen.request.RemoveRequest;
import com.ims.inventory.domen.request.UserRequest;
import com.ims.inventory.service.UserMasterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/user-management/user/")
public class UserController {

    @Autowired
    private UserMasterService userMasterService;

    @PostMapping("find/all")
    public ResponseEntity<?> findAll(
            @Valid @RequestBody FindUserRequest findUserRequest) throws Exception {
       return ResponseEntity.ok(userMasterService.findAllUserByIsActive(findUserRequest.isActive()));
    }

    @PostMapping("add")
    public ResponseEntity<?> addUser(
            @Valid @RequestBody UserRequest userRequest) throws Exception {
        try {
            return ResponseEntity.ok(userMasterService.addUser(userRequest));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
    }

    @PostMapping("edit")
    public ResponseEntity<?> editUser(
            @Valid @RequestBody UserRequest userRequest) throws Exception {
        try {
            return ResponseEntity.ok(userMasterService.editUser(userRequest));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(
            @Valid  @RequestBody RemoveRequest removeRequest) throws Exception {
        try {
            return ResponseEntity.ok(userMasterService.userDelete(removeRequest));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
    }




}
