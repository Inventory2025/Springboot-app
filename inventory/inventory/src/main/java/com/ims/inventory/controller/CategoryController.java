package com.ims.inventory.controller;

import com.ims.inventory.domen.request.*;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.service.impl.BranchMasterService;
import com.ims.inventory.service.impl.CategoryMasterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/category/")
public class CategoryController {

    @Autowired
    private CategoryMasterService categoryMasterService;

    @PostMapping("find/all")
    public ResponseEntity<?> findAll(
            @RequestBody FindCategoryRequest findCategoryRequest) throws Exception {
        return ResponseEntity.ok(categoryMasterService.findAllCategoryByIsActive(findCategoryRequest.isActive()));
    }

    @PostMapping("add")
    public ResponseEntity<?> addCategory(
            @Valid @RequestBody CategoryRequest categoryRequest) throws Exception {
        try {
            return ResponseEntity.ok(categoryMasterService.addCategory(categoryRequest));
        } catch (BadCredentialsException e) {
            throw new Exception("CategoryController::addCategory:Exception occurred while category creation.", e);
        }
    }

    @PostMapping("edit")
    public ResponseEntity<?> editCategory(
            @Valid @RequestBody CategoryRequest categoryRequest) throws Exception {
        try {
            return ResponseEntity.ok(categoryMasterService.editCategory(categoryRequest));
        } catch (BadCredentialsException e) {
            throw new Exception("CategoryController::addCategory:Exception occurred while category edition.", e);
        }
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> deleteCategory(
            @Valid @RequestBody RemoveRequest removeRequest) throws Exception {
        try {
            return ResponseEntity.ok(categoryMasterService.CategoryDelete(removeRequest));
        } catch (BadCredentialsException e) {
            throw new Exception("CategoryController::addCategory:Exception occurred while category deletion.", e);
        }
    }

    @PostMapping("load")
    public ResponseEntity<?> getSaleById(@Valid @RequestBody LoadRequest dto) throws ImsBusinessException {
        return ResponseEntity.ok(categoryMasterService.loadCategory(dto));
    }

    @PostMapping("categoryDropdown")
    public ResponseEntity<?> getCategoryDropDown(
            @RequestBody CategoryRequest categoryRequest) throws Exception {
        return ResponseEntity.ok(categoryMasterService.findAllCategory(categoryRequest));
    }
}
