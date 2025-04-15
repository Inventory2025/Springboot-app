package com.ims.inventory.service.impl;

import com.ims.inventory.domen.entity.CategoryMaster;
import com.ims.inventory.domen.entity.ProductMaster;
import com.ims.inventory.domen.request.CategoryRequest;
import com.ims.inventory.domen.request.ProductRequest;
import com.ims.inventory.domen.request.RemoveRequest;
import com.ims.inventory.domen.response.ApiResponse;
import com.ims.inventory.domen.response.CategoryResponse;
import com.ims.inventory.domen.response.ProductResponse;
import com.ims.inventory.domen.response.RoleResponse;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.mappers.RoleRowMapper;
import com.ims.inventory.repository.RoleRepository;
import com.ims.inventory.repository.impl.ProductRepository;
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
@Component("ProductMasterServiceImpl")
public class ProductMasterServiceImpl implements ProductMasterservice{

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductResponse> findAllProductByIsActive(Boolean isActive) throws ImsBusinessException {
        log.info("ProductMasterServiceImpl::findAllProductByIsActive:: called for isActive :{}", isActive);
        List<ProductMaster> productList = productRepository.findAll();
        if (!ObjectUtils.isEmpty(productList)) {
            return productList.stream().map(obj -> {
                ProductResponse product = new ProductResponse();
                product.setName(obj.getName());
                return product;
            }).toList();
        } else {
            log.info("ProductMasterServiceImpl::findAllProductByIsActive:: product data not found.");
            throw new ImsBusinessException("ProdOO1", "product not found.");
        }
    }
    private ProductResponse createResponse(ProductMaster productMaster, String method) throws ImsBusinessException {
        if (ObjectUtils.isNotEmpty(productMaster)) {
            ProductResponse resp = new ProductResponse();
            resp.setName(productMaster.getName());
            resp.setStatus(SUCCESS);
            resp.setMessage("Product " + method + "successfully.");
            return resp;
        } else {
            throw new ImsBusinessException(CATEGORY_NOT_FOUND_CODE,
                    "Product not " + method + "successfully.");
        }
    }

    @Override
    public ProductResponse addProduct(ProductRequest productRequest) throws Exception {
        log.info("ProductMasterService::addProduct request :{}", productRequest);
        try {
            ProductMaster productMaster = new ProductMaster();
            productMaster.setName(productMaster.getName());
            productMaster.setDescription(productMaster.getDescription());
            productMapper(productMaster, productRequest);
            ProductMaster product = productRepository.save(productMaster);
            log.info("ProductMasterService::addProduct:Product save successfully.");
            return createResponse(product, "Add");
        } catch (Exception e) {
            throw new ImsBusinessException(CATEGORY_ADD_EXCEPTION_CODE, CATEGORY_ADD_EXCEPTION_MSG);
        }
    }

    @Override
    public ProductResponse editProduct(ProductRequest productRequest) throws Exception {
        log.info("ProductMasterService::Edit product request :{}", productRequest);
        try {
            ProductMaster productMaster = loadProductByName(productRequest.getName());
            productMapper(productMaster, productRequest);
            productMaster = productRepository.save(productMaster);
            log.info("ProductService::addProduct:Product edit successfully.");
            return createResponse(productMaster, "Edit");
        } catch (Exception e) {
            log.error("ProductMasterService::editProduct::Exception occurred in edit Product for name :{}",
                    productRequest.getName(), e);
            throw new ImsBusinessException(CATEGORY_EDIT_EXCEPTION_CODE, CATEGORY_EDIT_EXCEPTION_MSG);
        }
    }

    @Override
    public ProductResponse ProductDelete(RemoveRequest removeRequest) throws Exception {
        log.info("ProductMasterService::ProductDelete:: delete product request :{}", removeRequest);
        try {
            ProductMaster productMasterObj = loadProductByName(removeRequest.getId());
            productRepository.delete(productMasterObj);
            ProductResponse resp = new ProductResponse();
            resp.setName(removeRequest.getId());
            resp.setStatus(SUCCESS);
            resp.setMessage("Product delete successfully.");
            log.info("ProductMasterService::addProduct:Product delete successfully.");
            return resp;
        } catch (Exception e) {
            throw new ImsBusinessException(CATEGORY_DELETE_EXCEPTION_CODE, CATEGORY_DELETE_EXCEPTION_MSG);
        }
    }

    private ProductMaster loadProductByName(String name) throws ImsBusinessException {
        log.info("ProductMasterService::loadProductByName:Load Product called.");
        Optional<ProductMaster> productMasterObj = productRepository.findByName(name);
        if (productMasterObj.isPresent() && ObjectUtils.isNotEmpty(productMasterObj.get())) {
            log.info("ProductMasterService::loadProductByName:Product found.");
            return productMasterObj.get();
        } else {
            throw new ImsBusinessException(CATEGORY_NOT_FOUND_CODE, CATEGORY_NOT_FOUND_MSG);
        }
    }
    private void productMapper(ProductMaster productMaster, ProductRequest productRequest) {
        log.info("ProductMasterService::productMapper:Product mapper called.");
        productMaster.setName(productRequest.getName());
        productMaster.setDescription(productRequest.getDescription());
    }
}
