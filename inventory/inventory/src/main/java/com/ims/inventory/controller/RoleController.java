package com.ims.inventory.controller;

import com.ims.inventory.domen.request.FetchRolePageRequest;
import com.ims.inventory.domen.request.FindRoleRequest;
import com.ims.inventory.domen.request.RemoveRequest;
import com.ims.inventory.domen.request.RoleRequest;
import com.ims.inventory.service.RoleMasterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/user-management/role/")
public class RoleController {

    @Autowired
    private RoleMasterService roleMasterService;

    @PostMapping("find/all")
    public ResponseEntity<?> findAll(
            @RequestBody FetchRolePageRequest fetchRolePageRequest) throws Exception {
        return ResponseEntity.ok(roleMasterService.findAllRoleByIsActive(fetchRolePageRequest));
    }

    @PostMapping("add")
    public ResponseEntity<?> addRole(
           @Valid @RequestBody RoleRequest roleRequest) throws Exception {
        try {
            return ResponseEntity.ok(roleMasterService.addRole(roleRequest));
        } catch (BadCredentialsException e) {
            throw new Exception("RoleController::addRole:Exception occurred while role creation.", e);
        }
    }

    @PostMapping("edit")
    public ResponseEntity<?> editRole(
            @Valid @RequestBody RoleRequest roleRequest) throws Exception {
        try {
            return ResponseEntity.ok(roleMasterService.editRole(roleRequest));
        } catch (BadCredentialsException e) {
            throw new Exception("RoleController::addRole:Exception occurred while role edition.", e);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteRole(
            @Valid @RequestBody RemoveRequest removeRequest) throws Exception {
        try {
            return ResponseEntity.ok(roleMasterService.roleDelete(removeRequest));
        } catch (BadCredentialsException e) {
            throw new Exception("RoleController::addRole:Exception occurred while role deletion.", e);
        }
    }

}
