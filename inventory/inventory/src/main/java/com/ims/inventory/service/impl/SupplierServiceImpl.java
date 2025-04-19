package com.ims.inventory.service.impl;

import com.ims.inventory.domen.entity.CustomerMaster;
import com.ims.inventory.domen.entity.SupplierMaster;
import com.ims.inventory.domen.request.CustomerRequest;
import com.ims.inventory.domen.request.SupplierRequest;
import com.ims.inventory.domen.response.AutoCompleteResponse;
import com.ims.inventory.domen.response.CustomerResponse;
import com.ims.inventory.domen.response.SupplierResponse;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

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

        supplier.setSupplierName(dto.getSupplierName());
        supplier.setEmail(dto.getEmail());
        supplier.setPhoneNumber(dto.getPhoneNumber());
        supplier.setAddressLine1(dto.getAddressLine1());
        supplier.setAddressLine2(dto.getAddressLine2());
        supplier.setAddressLine3(dto.getAddressLine3());
        supplier.setPinCode(dto.getPinCode());
        supplier.setActive(true);

        supplier.setCity(cityRepository.findByCode(dto.getCity()).orElse(null));
        supplier.setState(stateRepository.findByCode(dto.getState()).orElse(null));
        supplier.setCountry(countryRepository.findByCode(dto.getCountry()).orElse(null));

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

        supplier.setSupplierName(dto.getSupplierName());
        supplier.setEmail(dto.getEmail());
        supplier.setPhoneNumber(dto.getPhoneNumber());
        supplier.setAddressLine1(dto.getAddressLine1());
        supplier.setAddressLine2(dto.getAddressLine2());
        supplier.setAddressLine3(dto.getAddressLine3());
        supplier.setPinCode(dto.getPinCode());
        supplier.setActive(true);

        supplier.setCity(cityRepository.findByCode(dto.getCity()).orElse(null));
        supplier.setState(stateRepository.findByCode(dto.getState()).orElse(null));
        supplier.setCountry(countryRepository.findByCode(dto.getCountry()).orElse(null));

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
}
