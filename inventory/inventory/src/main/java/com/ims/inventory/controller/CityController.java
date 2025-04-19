package com.ims.inventory.controller;

import com.ims.inventory.domen.request.CityRequest;
import com.ims.inventory.domen.request.CountryRequest;
import com.ims.inventory.domen.request.LoadRequest;
import com.ims.inventory.domen.request.RemoveRequest;
import com.ims.inventory.service.impl.CityMasterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/city")
@RequiredArgsConstructor
public class CityController {

    @Autowired
    CityMasterService cityMasterService;

    @PostMapping("add")
    public ResponseEntity<?> addCity(
            @Valid @RequestBody CityRequest cityRequest , HttpServletRequest request) throws Exception {
        try {
            return ResponseEntity.ok(cityMasterService.addCity(cityRequest ,request));
        } catch (BadCredentialsException e) {
            throw new Exception("RoleController::addRole:Exception occurred while role creation.", e);
        }

    }

    @PostMapping("edit")
    public ResponseEntity<?> editCity(
            @Valid @RequestBody CityRequest cityRequest, HttpServletRequest request) throws Exception {
        try {
            return ResponseEntity.ok(cityMasterService.editCity(cityRequest, request));
        } catch (BadCredentialsException e) {
            throw new Exception("RoleController::addRole:Exception occurred while role edition.", e);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCity(
            @Valid @RequestBody RemoveRequest removeRequest, HttpServletRequest request) throws Exception {
        try {
            return ResponseEntity.ok(cityMasterService.CityDelete(removeRequest, request));
        } catch (BadCredentialsException e) {
            throw new Exception("RoleController::addRole:Exception occurred while role deletion.", e);
        }
    }

    @PostMapping("load")
    public ResponseEntity<?> loadCity(
            @Valid @RequestBody LoadRequest loadRequest, HttpServletRequest request) throws Exception {
        try {
            return ResponseEntity.ok(cityMasterService.load(loadRequest, request));
        } catch (BadCredentialsException e) {
            throw new Exception("RoleController::addRole:Exception occurred while role edition.", e);
        }
    }
}
