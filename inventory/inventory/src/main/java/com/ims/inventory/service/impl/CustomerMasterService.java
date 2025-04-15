package com.ims.inventory.service.impl;

import com.ims.inventory.domen.request.CategoryRequest;
import com.ims.inventory.domen.request.CustomerRequest;
import com.ims.inventory.domen.request.RemoveRequest;
import com.ims.inventory.domen.response.CategoryResponse;
import com.ims.inventory.domen.response.CustomerResponse;
import com.ims.inventory.exception.ImsBusinessException;

import java.util.List;

public interface CustomerMasterService {
    List<CustomerResponse> findAllCustomerByIsActive(Boolean isActive) throws ImsBusinessException;

    public CustomerResponse addCustomer(CustomerRequest customerRequest) throws Exception;

    public CustomerResponse editCustomer(CustomerRequest customerRequest) throws Exception;

    CustomerResponse CustomerDelete(RemoveRequest removeRequest) throws Exception;
}
