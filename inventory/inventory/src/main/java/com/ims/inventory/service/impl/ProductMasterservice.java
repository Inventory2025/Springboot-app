package com.ims.inventory.service.impl;

import com.ims.inventory.domen.request.LocationRequest;
import com.ims.inventory.domen.request.ProductRequest;
import com.ims.inventory.domen.request.RemoveRequest;
import com.ims.inventory.domen.response.AutoCompleteResponse;
import com.ims.inventory.domen.response.LocationResponse;
import com.ims.inventory.domen.response.ProductDetailResponse;
import com.ims.inventory.domen.response.ProductResponse;
import com.ims.inventory.exception.ImsBusinessException;

import java.util.List;

public interface ProductMasterservice {
    List<ProductResponse> findAllProductByIsActive(Boolean isActive) throws ImsBusinessException;

    public ProductResponse addProduct(ProductRequest productRequest) throws Exception;

    public ProductResponse editProduct(ProductRequest productRequest) throws Exception;

    ProductDetailResponse loadProduct(ProductRequest productRequest) throws Exception;

    ProductResponse ProductDelete(RemoveRequest removeRequest) throws Exception;

    List<AutoCompleteResponse> findAllProductByNameIsActive(String name, Boolean isActive) throws ImsBusinessException;
}
