package com.ims.inventory.controller;

import com.ims.inventory.domen.request.*;
import com.ims.inventory.service.RoleMasterService;
import com.ims.inventory.service.impl.LocationMasterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/user-management/location/")
public class LocationController {

    @Autowired
    private LocationMasterService locationMasterService;

    @PostMapping("find/all")
    public ResponseEntity<?> findAll(
            @RequestBody FindLocationRequest findLocationRequest) throws Exception {
        return ResponseEntity.ok(locationMasterService.findAllLocationByIsActive(findLocationRequest.isActive()));
    }

    @PostMapping("add")
    public ResponseEntity<?> addLocation(
            @Valid @RequestBody LocationRequest locationRequest) throws Exception {
        try {
            return ResponseEntity.ok(locationMasterService.addLocation(locationRequest));
        } catch (BadCredentialsException e) {
            throw new Exception("LocationController::addLocation:Exception occurred while location creation.", e);
        }
    }

    @PostMapping("edit")
    public ResponseEntity<?> editLocation(
            @Valid @RequestBody LocationRequest locationRequest) throws Exception {
        try {
            return ResponseEntity.ok(locationMasterService.editLocation(locationRequest));
        } catch (BadCredentialsException e) {
            throw new Exception("LocationController::addLocation:Exception occurred while location edition.", e);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> LocationDelete(
            @Valid @RequestBody RemoveRequest removeRequest) throws Exception {
        try {
            return ResponseEntity.ok(locationMasterService.LocationDelete(removeRequest));
        } catch (BadCredentialsException e) {
            throw new Exception("LocationController::addLocation:Exception occurred while location deletion.", e);
        }
    }
}
