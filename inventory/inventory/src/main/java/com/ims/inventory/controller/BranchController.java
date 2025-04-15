package com.ims.inventory.controller;

import com.ims.inventory.domen.request.*;
import com.ims.inventory.service.RoleMasterService;
import com.ims.inventory.service.impl.BranchMasterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/user-management/branch/")
public class BranchController {

    @Autowired
    private BranchMasterService branchMasterService;

    @PostMapping("find/all")
    public ResponseEntity<?> findAll(
            @RequestBody FindBranchRequest findBranchRequest) throws Exception {
        return ResponseEntity.ok(branchMasterService.findAllBranchByIsActive(findBranchRequest.isActive()));
    }

    @PostMapping("add")
    public ResponseEntity<?> addRole(
            @Valid @RequestBody BranchRequest branchRequest) throws Exception {
        try {
            return ResponseEntity.ok(branchMasterService.addBranch(branchRequest));
        } catch (BadCredentialsException e) {
            throw new Exception("BranchController::addBranch:Exception occurred while branch creation.", e);
        }
    }

    @PostMapping("edit")
    public ResponseEntity<?> editBranch(
            @Valid @RequestBody BranchRequest branchRequest) throws Exception {
        try {
            return ResponseEntity.ok(branchMasterService.editBranch(branchRequest));
        } catch (BadCredentialsException e) {
            throw new Exception("BranchController::addBranch:Exception occurred while branch edition.", e);
        }
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> deleteBranch(
            @Valid @RequestBody RemoveRequest removeRequest) throws Exception {
        try {
            return ResponseEntity.ok(branchMasterService.BranchDelete(removeRequest));
        } catch (BadCredentialsException e) {
            throw new Exception("BranchController::addBranch:Exception occurred while branch deletion.", e);
        }
    }
}
