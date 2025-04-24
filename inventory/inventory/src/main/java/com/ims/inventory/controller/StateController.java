package com.ims.inventory.controller;

import com.ims.inventory.domen.request.CityRequest;
import com.ims.inventory.domen.request.LoadRequest;
import com.ims.inventory.domen.request.RemoveRequest;
import com.ims.inventory.domen.request.StateRequest;
import com.ims.inventory.service.impl.StateMasterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/state")
@RequiredArgsConstructor
public class StateController {

    @Autowired
    StateMasterService stateMasterService;

    @PostMapping("add")
    public ResponseEntity<?> addState(
            @Valid @RequestBody StateRequest stateRequest, HttpServletRequest request) throws Exception {
        try {
            return ResponseEntity.ok(stateMasterService.addState(stateRequest, request));
        } catch (BadCredentialsException e) {
            throw new Exception("RoleController::addRole:Exception occurred while role creation.", e);
        }
    }

    @PostMapping("edit")
    public ResponseEntity<?> editState(
            @Valid @RequestBody StateRequest stateRequest, HttpServletRequest request) throws Exception {
        try {
            return ResponseEntity.ok(stateMasterService.editState(stateRequest, request));
        } catch (BadCredentialsException e) {
            throw new Exception("RoleController::addRole:Exception occurred while role edition.", e);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteState(
            @Valid @RequestBody RemoveRequest removeRequest, HttpServletRequest request) throws Exception {
        try {
            return ResponseEntity.ok(stateMasterService.stateDelete(removeRequest, request));
        } catch (BadCredentialsException e) {
            throw new Exception("RoleController::addRole:Exception occurred while role deletion.", e);
        }
    }

    @PostMapping("load")
    public ResponseEntity<?> loadState(
            @Valid @RequestBody LoadRequest loadRequest, HttpServletRequest request) throws Exception {
        try {
            return ResponseEntity.ok(stateMasterService.load(loadRequest, request));
        } catch (BadCredentialsException e) {
            throw new Exception("RoleController::addRole:Exception occurred while role edition.", e);
        }
    }

    @PostMapping("stateDropdown")
    public ResponseEntity<?> getstateDropDown(
            @RequestBody StateRequest stateRequest) throws Exception {
        return ResponseEntity.ok(stateMasterService.findAllState(stateRequest));
    }
}
