package com.ims.inventory.service.impl;

import com.ims.inventory.domen.request.BranchRequest;
import com.ims.inventory.domen.request.CategoryRequest;
import com.ims.inventory.domen.request.RemoveRequest;
import com.ims.inventory.domen.response.BranchResponse;
import com.ims.inventory.domen.response.CategoryResponse;
import com.ims.inventory.exception.ImsBusinessException;

import java.util.List;

public interface CategoryMasterService {
    List<CategoryResponse> findAllCategoryByIsActive(Boolean isActive) throws ImsBusinessException;

    public CategoryResponse addCategory(CategoryRequest categoryRequest) throws Exception;

    public CategoryResponse editCategory(CategoryRequest categoryRequest) throws Exception;

    CategoryResponse CategoryDelete(RemoveRequest removeRequest) throws Exception;
}
