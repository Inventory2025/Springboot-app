package com.ims.inventory.service.impl;

import com.ims.inventory.domen.entity.BranchMaster;
import com.ims.inventory.domen.entity.CategoryMaster;
import com.ims.inventory.domen.request.BranchRequest;
import com.ims.inventory.domen.request.CategoryRequest;
import com.ims.inventory.domen.request.RemoveRequest;
import com.ims.inventory.domen.response.BranchResponse;
import com.ims.inventory.domen.response.CategoryResponse;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.repository.BranchRepository;
import com.ims.inventory.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.ims.inventory.constants.ErrorCode.*;
import static com.ims.inventory.constants.ErrorMsg.*;
import static com.ims.inventory.constants.ImsConstants.SUCCESS;

@Slf4j
@Component("CategoryMasterServiceImpl")
public class CategoryMasterServiceImpl implements CategoryMasterService{

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public List<CategoryResponse> findAllCategoryByIsActive(Boolean isActive) throws ImsBusinessException {
        log.info("CategoryMasterServiceImpl::findAllCategoryByIsActive:: called for isActive :{}", isActive);
        List<CategoryMaster> categoryList = categoryRepository.findAll();
        if (!ObjectUtils.isEmpty(categoryList)) {
            return categoryList.stream().map(obj -> {
                CategoryResponse category = new CategoryResponse();
                category.setName(obj.getName());
                return category;
            }).toList();
        } else {
            log.info("CategoryMasterServiceImpl::findAllCategoryByIsActive:: category data not found.");
            throw new ImsBusinessException("CatOO1", "category not found.");
        }
    }
    private CategoryResponse createResponse(CategoryMaster categoryMaster, String method) throws ImsBusinessException {
        if (ObjectUtils.isNotEmpty(categoryMaster)) {
            CategoryResponse resp = new CategoryResponse();
            resp.setName(categoryMaster.getName());
            resp.setStatus(SUCCESS);
            resp.setMessage("Category " + method + "successfully.");
            return resp;
        } else {
            throw new ImsBusinessException(CATEGORY_NOT_FOUND_CODE,
                    "Category not " + method + "successfully.");
        }
    }

    @Override
    public CategoryResponse addCategory(CategoryRequest categoryRequest) throws Exception {
        log.info("CategoryMasterService::addCategory request :{}", categoryRequest);
        try {
            CategoryMaster categoryMaster = new CategoryMaster();
            categoryMaster.setName(categoryMaster.getName());
            categoryMaster.setDescription(categoryMaster.getDescription());
            categoryMapper(categoryMaster, categoryRequest);
            CategoryMaster category = categoryRepository.save(categoryMaster);
            log.info("CategoryMasterService::addCategory:Category save successfully.");
            return createResponse(category, "Add");
        } catch (Exception e) {
            throw new ImsBusinessException(CATEGORY_ADD_EXCEPTION_CODE, CATEGORY_ADD_EXCEPTION_MSG);
        }
    }

    @Override
    public CategoryResponse editCategory(CategoryRequest categoryRequest) throws Exception {
        log.info("CategoryMasterService::Edit category request :{}", categoryRequest);
        try {
            CategoryMaster categoryMaster = loadCategoryByName(categoryRequest.getName());
            categoryMapper(categoryMaster, categoryRequest);
            categoryMaster = categoryRepository.save(categoryMaster);
            log.info("CategoryService::addCategory:Category edit successfully.");
            return createResponse(categoryMaster, "Edit");
        } catch (Exception e) {
            log.error("CategoryMasterService::editCategory::Exception occurred in edit Category for name :{}",
                    categoryRequest.getName(), e);
            throw new ImsBusinessException(CATEGORY_EDIT_EXCEPTION_CODE, CATEGORY_EDIT_EXCEPTION_MSG);
        }
    }

    @Override
    public CategoryResponse CategoryDelete(RemoveRequest removeRequest) throws Exception {
        log.info("CategoryMasterService::CategoryDelete:: delete category request :{}", removeRequest);
        try {
            CategoryMaster categoryMasterObj = loadCategoryByName(removeRequest.getId());
            categoryRepository.delete(categoryMasterObj);
            CategoryResponse resp = new CategoryResponse();
            resp.setName(removeRequest.getId());
            resp.setStatus(SUCCESS);
            resp.setMessage("Category delete successfully.");
            log.info("CategoryMasterService::addCategory:Category delete successfully.");
            return resp;
        } catch (Exception e) {
            throw new ImsBusinessException(CATEGORY_DELETE_EXCEPTION_CODE, CATEGORY_DELETE_EXCEPTION_MSG);
        }
    }
    private CategoryMaster loadCategoryByName(String name) throws ImsBusinessException {
        log.info("CategoryMasterService::loadCategoryByName:Load Category called.");
        Optional<CategoryMaster> categoryMasterObj = categoryRepository.findByName(name);
        if (categoryMasterObj.isPresent() && ObjectUtils.isNotEmpty(categoryMasterObj.get())) {
            log.info("CategoryMasterService::loadCategoryByName:Category found.");
            return categoryMasterObj.get();
        } else {
            throw new ImsBusinessException(CATEGORY_NOT_FOUND_CODE, CATEGORY_NOT_FOUND_MSG);
        }
    }
    private void categoryMapper(CategoryMaster categoryMaster, CategoryRequest categoryRequest) {
        log.info("CategoryMasterService::categoryMapper:Category mapper called.");
        categoryMaster.setName(categoryRequest.getName());
        categoryMaster.setDescription(categoryRequest.getDescription());
    }
}
