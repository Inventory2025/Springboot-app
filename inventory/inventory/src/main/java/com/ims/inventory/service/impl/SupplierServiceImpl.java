package com.ims.inventory.service.impl;

import com.ims.inventory.domen.entity.*;
import com.ims.inventory.domen.request.CustomerRequest;
import com.ims.inventory.domen.request.LoadRequest;
import com.ims.inventory.domen.request.SupplierRequest;
import com.ims.inventory.domen.response.AutoCompleteResponse;
import com.ims.inventory.domen.response.CustomerResponse;
import com.ims.inventory.domen.response.SupplierResponse;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.ims.inventory.constants.ErrorCode.*;
import static com.ims.inventory.constants.ErrorMsg.BRANCH_EDIT_EXCEPTION_MSG;
import static com.ims.inventory.constants.ErrorMsg.BRANCH_NOT_FOUND_MSG;
import static com.ims.inventory.constants.ImsConstants.SUCCESS;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupplierServiceImpl {

    private final SupplierRepository supplierRepository;
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;

    public SupplierResponse create(SupplierRequest dto) {
        log.info("SupplierServiceImpl::create:: Supplier insert method call.");
        SupplierMaster supplier = new SupplierMaster();

        supplier.setSupplierName(dto.getName());
        supplier.setEmail(dto.getEmail());
        supplier.setPhoneNumber(dto.getPhoneNumber());
        supplier.setActive(true);

        supplier.setCity(cityRepository.findById(dto.getCityId()).orElse(null));
        supplier.setState(stateRepository.findById(dto.getStateId()).orElse(null));
        supplier.setCountry(countryRepository.findById(dto.getCountryId()).orElse(null));

        supplierRepository.save(supplier);
        return mapToDto(supplier);
    }

    public List<SupplierResponse> getAll() {
        return supplierRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public SupplierResponse getById(String id) {
        return supplierRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
    }

    public SupplierResponse update(String id, SupplierRequest dto) {
        log.info("SupplierServiceImpl::update:: Supplier update method call.");
        SupplierMaster supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        supplier.setSupplierName(dto.getName());
        supplier.setEmail(dto.getEmail());
        supplier.setPhoneNumber(dto.getPhoneNumber());
        supplier.setActive(true);

        supplier.setCity(cityRepository.findByCode(dto.getCityId()).orElse(null));
        supplier.setState(stateRepository.findByCode(dto.getStateId()).orElse(null));
        supplier.setCountry(countryRepository.findByCode(dto.getCountryId()).orElse(null));

        return mapToDto(supplierRepository.save(supplier));
    }

    public void softDelete(String id) {
        SupplierMaster supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        supplier.setActive(false);
        supplierRepository.save(supplier);
    }

    private SupplierResponse mapToDto(SupplierMaster c) {
        log.info("SupplierServiceImpl::mapToDto:: Supplier mapToDto method call.");
        return SupplierResponse.builder()
                .supplierName(c.getSupplierName())
                .email(c.getEmail())
                .phoneNumber(c.getPhoneNumber())
                .addressLine1(c.getAddressLine1())
                .addressLine2(c.getAddressLine2())
                .addressLine3(c.getAddressLine3())
                .city(c.getCity() != null ? c.getCity().getName() : null)
                .state(c.getState() != null ? c.getState().getName() : null)
                .country(c.getCountry() != null ? c.getCountry().getName() : null)
                .pinCode(c.getPinCode())
                .isActive(c.isActive())
                .createAt(c.getCreateAt())
                .lastModifiedAt(c.getLastModifiedAt())
                .createdBy(c.getCreatedBy())
                .lastModifiedBy(c.getLastModifiedBy())
                .version(c.getVersion())
                .build();
    }

    public List<AutoCompleteResponse> findAllSupplierByNameIsActive(String name, Boolean isActive) throws ImsBusinessException {
        log.info("SupplierServiceImpl::findAllSupplierByNameIsActive:: Supplier autocomplete method call.");
        List<SupplierMaster> supplierList = null;
        if (StringUtils.isEmpty(name)) {
            supplierList = supplierRepository.findTop15ByIsActiveOrderBySupplierNameAsc(isActive);
        } else {
            supplierList = supplierRepository.findByIsActiveAndSupplierNameIgnoreCaseContaining(isActive, name);
        }
        if (!ObjectUtils.isEmpty(supplierList)) {
            return supplierList.stream().map(obj -> {
                AutoCompleteResponse resp = new AutoCompleteResponse();
                resp.setId(obj.getId());
                resp.setName(obj.getSupplierName());
                resp.setOption(obj.getId());
                return resp;
            }).toList();
        } else {
            log.info("SupplierServiceImpl::findAllSupplierByNameIsActive:: search product data not found.");
            throw new ImsBusinessException("CUSTOO1", "Customer not found.");
        }
    }

    public SupplierResponse editSupplier( SupplierRequest dto) throws Exception {
        log.info("SupplierMasterService::Edit branch request :{}", dto);
        try {
            SupplierMaster supplierMaster = loadSupplierByName(dto.getName());
            supplierMapper(supplierMaster, dto);
            supplierMaster = supplierRepository.save(supplierMaster);
            log.info("customerService::addCustomer:Customer edit successfully.");
            return createResponse(supplierMaster, "Edit");
        } catch (Exception e) {
            log.error("SupplierMasterService::editsupplier::Exception occurred in edit Supplier for name :{}",
                    dto.getName(), e);
            throw new ImsBusinessException(BRANCH_EDIT_EXCEPTION_CODE, BRANCH_EDIT_EXCEPTION_MSG);
        }
    }

    private void supplierMapper(SupplierMaster supplierMaster, SupplierRequest dto) {
        supplierMaster.setSupplierName(dto.getName());
        supplierMaster.setPhoneNumber(dto.getPhoneNumber());
        supplierMaster.setEmail(dto.getEmail());
        supplierMaster.setCity(cityRepository.findById(dto.getCityId()).orElse(null));
        supplierMaster.setCountry(countryRepository.findById(dto.getCountryId()).orElse(null));
        supplierMaster.setState(stateRepository.findById(dto.getStateId()).orElse(null));
    }

    private SupplierMaster loadSupplierByName(String name) throws ImsBusinessException {
        log.info("SupplierMasterService::loadSupplierByName:Load customer called.");
        Optional<SupplierMaster> supplierMasterObj = supplierRepository.findBySupplierName(name);
        if (supplierMasterObj.isPresent() && ObjectUtils.isNotEmpty(supplierMasterObj.get())) {
            log.info("SupplierMasterService::loadCustomerByName:customer found.");
            return supplierMasterObj.get();
        } else {
            throw new ImsBusinessException(BRANCH_NOT_FOUND_CODE, BRANCH_NOT_FOUND_MSG);
        }
    }

    private SupplierResponse createResponse(SupplierMaster supplierMaster, String method) throws ImsBusinessException {
        if (ObjectUtils.isNotEmpty(supplierMaster)) {
            SupplierResponse resp = new SupplierResponse();
            resp.setSupplierName(supplierMaster.getSupplierName());
            resp.setStatus(SUCCESS);
            resp.setMessage("supplier " + method + "successfully.");
            return resp;
        } else {
            throw new ImsBusinessException(USER_NOT_FOUND_CODE,
                    "supplier not " + method + "successfully.");
        }
    }

    public SupplierRequest loadSupplier(LoadRequest loadRequest, HttpServletRequest request) throws ImsBusinessException {
        SupplierMaster supplierTran = supplierRepository.findByIdAndIsActive(loadRequest.getRecordCode(), true);
        if (ObjectUtils.isNotEmpty(supplierTran)) {
            return mapperDto(supplierTran);
        } else {
            throw new ImsBusinessException("Sale01", "Sale not found for id :"+loadRequest.getRecordCode());
        }
    }

    private SupplierRequest mapperDto(SupplierMaster supplierTran) {
        SupplierRequest supplier = new SupplierRequest();
        supplier.setName(supplierTran.getSupplierName());
        supplier.setPhoneNumber(supplierTran.getPhoneNumber());
        supplier.setEmail(supplierTran.getEmail());

        City cityMaster = cityRepository.findByIdAndIsActive(supplierTran.getCity().getId(), true);
        supplier.setCityId(cityMaster.getId());

        State stateMaster = stateRepository.findByIdAndIsActive(supplierTran.getState().getId(), true);
        supplier.setStateId(stateMaster.getId());

        Country CountryMaster = countryRepository.findByIdAndIsActive(supplierTran.getCountry().getId(), true);
        supplier.setCountryId(CountryMaster.getId());

        return supplier;
    }


}
