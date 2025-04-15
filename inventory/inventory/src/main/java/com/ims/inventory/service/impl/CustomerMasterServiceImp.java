package com.ims.inventory.service.impl;

import com.ims.inventory.domen.entity.CategoryMaster;
import com.ims.inventory.domen.entity.CustomerMaster;
import com.ims.inventory.domen.request.CategoryRequest;
import com.ims.inventory.domen.request.CustomerRequest;
import com.ims.inventory.domen.request.RemoveRequest;
import com.ims.inventory.domen.response.CategoryResponse;
import com.ims.inventory.domen.response.CustomerResponse;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.repository.CategoryRepository;
import com.ims.inventory.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.ims.inventory.constants.ErrorCode.CATEGORY_ADD_EXCEPTION_CODE;
import static com.ims.inventory.constants.ErrorCode.CATEGORY_NOT_FOUND_CODE;
import static com.ims.inventory.constants.ErrorMsg.CATEGORY_ADD_EXCEPTION_MSG;
import static com.ims.inventory.constants.ErrorMsg.CATEGORY_NOT_FOUND_MSG;
import static com.ims.inventory.constants.ImsConstants.SUCCESS;

@Slf4j
@Component("CustomerMasterServiceImp")
public class CustomerMasterServiceImp implements CustomerMasterService{

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<CustomerResponse> findAllCustomerByIsActive(Boolean isActive) throws ImsBusinessException {
        log.info("CustomerMasterServiceImpl::findAllCustomerByIsActive:: called for isActive :{}", isActive);
        List<CustomerMaster> customerList = customerRepository.findAll();
        if (!ObjectUtils.isEmpty(customerList)) {
            return customerList.stream().map(obj -> {
                CustomerResponse customer = new CustomerResponse();
                customer.setCustomer_Name(obj.getCustomerName());
                return customer;
            }).toList();
        } else {
            log.info("CustomerMasterServiceImpl::findAllCustomerByIsActive:: customer data not found.");
            throw new ImsBusinessException("CustOO1", "customer not found.");
        }
    }
    private CustomerResponse createResponse(CustomerMaster customerMaster, String method) throws ImsBusinessException {
        if (ObjectUtils.isNotEmpty(customerMaster)) {
            CustomerResponse resp = new CustomerResponse();
            resp.setCustomer_Name(customerMaster.getCustomerName());
            resp.setStatus(SUCCESS);
            resp.setMessage("Customer " + method + "successfully.");
            return resp;
        } else {
            throw new ImsBusinessException(CATEGORY_NOT_FOUND_CODE,
                    "Customer not " + method + "successfully.");
        }
    }

    @Override
    public CustomerResponse addCustomer(CustomerRequest customerRequest) throws Exception {
        log.info("CustomerMasterService::addCustomer request :{}", customerRequest);
        try {
            CustomerMaster customerMaster = new CustomerMaster();
            customerMaster.setCustomerName(customerMaster.getCustomerName());
            customerMaster.setEmail(customerMaster.getEmail());
            customerMaster.setPhoneNumber(customerMaster.getPhoneNumber());
            customerMaster.setCountry(customerMaster.getCountry());
            customerMaster.setCity(customerMaster.getCity());
           // customerMapper(categoryMaster, categoryRequest);
           // CategoryMaster category = categoryRepository.save(categoryMaster);
            log.info("CategoryMasterService::addCategory:Category save successfully.");
            return null;// createResponse("Add");
        } catch (Exception e) {
            throw new ImsBusinessException(CATEGORY_ADD_EXCEPTION_CODE, CATEGORY_ADD_EXCEPTION_MSG);
        }
    }

    @Override
    public CustomerResponse editCustomer(CustomerRequest customerRequest) throws Exception {
        return null;
    }

    @Override
    public CustomerResponse CustomerDelete(RemoveRequest removeRequest) throws Exception {
        return null;
    }
    private CustomerMaster loadCustomerByCustomer_Name(String Customer_Name) throws ImsBusinessException {
        log.info("CustomerMasterService::loadCustomerByCustomer_Name:Load Customer called.");
        Optional<CustomerMaster> customerMasterObj = null; //customerRepository.findBycustomer_Name(Customer_Name);
        if (customerMasterObj.isPresent() && ObjectUtils.isNotEmpty(customerMasterObj.get())) {
            log.info("CustomerMasterService::loadCustomerByCustomer_Name:Customer found.");
            return customerMasterObj.get();
        } else {
            throw new ImsBusinessException(CATEGORY_NOT_FOUND_CODE, CATEGORY_NOT_FOUND_MSG);
        }
    }
    private void customerMapper(CustomerMaster customerMaster, CustomerRequest customerRequest) {
        log.info("CustomerMasterService::customerMapper:Customer mapper called.");
        customerMaster.setCustomerName(customerMaster.getCustomerName());
        customerMaster.setEmail(customerMaster.getEmail());
        customerMaster.setPhoneNumber(customerMaster.getPhoneNumber());
        customerMaster.setCountry(customerMaster.getCountry());
        customerMaster.setCity(customerMaster.getCity());

    }
}
