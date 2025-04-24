package com.ims.inventory.service.impl;

import com.ims.inventory.domen.dto.CustomerImportDto;
import com.ims.inventory.domen.entity.*;
import com.ims.inventory.domen.request.BranchRequest;
import com.ims.inventory.domen.request.CustomerRequest;
import com.ims.inventory.domen.request.LoadRequest;
import com.ims.inventory.domen.request.UserRequest;
import com.ims.inventory.domen.response.AutoCompleteResponse;
import com.ims.inventory.domen.response.BranchResponse;
import com.ims.inventory.domen.response.CustomerResponse;
import com.ims.inventory.domen.response.UserResponse;
import com.ims.inventory.exception.ImportError;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.repository.CityRepository;
import com.ims.inventory.repository.CountryRepository;
import com.ims.inventory.repository.CustomerRepository;
import com.ims.inventory.repository.StateRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.ims.inventory.constants.ErrorCode.*;
import static com.ims.inventory.constants.ErrorMsg.BRANCH_EDIT_EXCEPTION_MSG;
import static com.ims.inventory.constants.ErrorMsg.BRANCH_NOT_FOUND_MSG;
import static com.ims.inventory.constants.ImsConstants.SUCCESS;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl {

    private final CustomerRepository customerRepository;
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;

    public CustomerResponse create(CustomerRequest dto) {
        CustomerMaster customer = new CustomerMaster();

        customer.setCustomerName(dto.getCustomerName());
        customer.setEmail(dto.getEmail());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setActive(true);

        customer.setCity(cityRepository.findById(dto.getCityId()).orElse(null));
        customer.setState(stateRepository.findById(dto.getStateId()).orElse(null));
        customer.setCountry(countryRepository.findById(dto.getCountryId()).orElse(null));

        customerRepository.save(customer);
        return mapToDto(customer);
    }

    public List<CustomerResponse> getAll() {
        return customerRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public CustomerResponse getById(String id) {
        return customerRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public CustomerResponse update(String id, CustomerRequest dto) {
        CustomerMaster customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setCustomerName(dto.getCustomerName());
        customer.setEmail(dto.getEmail());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setActive(true);

        customer.setCity(cityRepository.findById(dto.getCityId()).orElse(null));
        customer.setState(stateRepository.findByCode(dto.getStateId()).orElse(null));
        customer.setCountry(countryRepository.findByCode(dto.getCountryId()).orElse(null));

        return mapToDto(customerRepository.save(customer));
    }

    public void softDelete(String id) {
        CustomerMaster customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setActive(false);
        customerRepository.save(customer);
    }

    private CustomerResponse mapToDto(CustomerMaster c) {
        return CustomerResponse.builder()
                .customerName(c.getCustomerName())
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

    public void exportToCsv(HttpServletResponse response) throws IOException {
        List<CustomerMaster> customers = customerRepository.findAllByIsActive(true);

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=customers.csv");

        PrintWriter writer = response.getWriter();
        writer.println("Name,Email,Phone,Address1,City,State,Country,PinCode");

        for (CustomerMaster c : customers) {
            writer.printf("%s,%s,%s,%s,%s,%s,%s,%s%n",
                    c.getCustomerName(),
                    c.getEmail(),
                    c.getPhoneNumber(),
                    c.getAddressLine1(),
                    c.getCity() != null ? c.getCity().getName() : "",
                    c.getState() != null ? c.getState().getName() : "",
                    c.getCountry() != null ? c.getCountry().getName() : "",
                    c.getPinCode());
        }
        writer.flush();
    }

    public List<ImportError> importFromCsv(MultipartFile file) throws IOException {
        List<ImportError> errors = new ArrayList<>();
        int rowNumber = 1;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                rowNumber++;
                String[] data = line.split(",");

                try {
                    CustomerImportDto dto = new CustomerImportDto();
                    dto.setCustomerName(data[0].trim());
                    dto.setEmail(data[1].trim());
                    dto.setPhoneNumber(data[2].trim());
                    dto.setAddressLine1(data[3].trim());
                    dto.setCity(data[4].trim());
                    dto.setState(data[5].trim());
                    dto.setCountry(data[6].trim());
                    dto.setPinCode(data[7].trim());

                    validateDto(dto, rowNumber, errors);

                    int finalRowNumber = rowNumber;
                    if (errors.stream().noneMatch(e -> e.getRowNumber() == finalRowNumber)) {
                        CustomerMaster customer = new CustomerMaster();
                        customer.setCustomerName(dto.getCustomerName());
                        customer.setEmail(dto.getEmail());
                        customer.setPhoneNumber(dto.getPhoneNumber());
                        customer.setAddressLine1(dto.getAddressLine1());
                        customer.setCity(cityRepository.findByCode(dto.getCity()).orElse(null));
                        customer.setState(stateRepository.findByCode(dto.getState()).orElse(null));
                        customer.setCountry(countryRepository.findByCode(dto.getCountry()).orElse(null));
                        customer.setPinCode(dto.getPinCode());
                        customer.setActive(true);

                        customerRepository.save(customer);
                    }

                } catch (Exception e) {
                    errors.add(new ImportError(rowNumber, "Unexpected error: " + e.getMessage()));
                }
            }
        }
        return errors;
    }

    private void validateDto(CustomerImportDto dto, int row, List<ImportError> errors) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<CustomerImportDto>> violations = validator.validate(dto);
        for (ConstraintViolation<CustomerImportDto> v : violations) {
            errors.add(new ImportError(row, v.getMessage()));
        }

        if (cityRepository.findByCode(dto.getCity()).isEmpty())
            errors.add(new ImportError(row, "City not found: " + dto.getCity()));

        if (stateRepository.findByCode(dto.getState()).isEmpty())
            errors.add(new ImportError(row, "State not found: " + dto.getState()));

        if (countryRepository.findByCode(dto.getCountry()).isEmpty())
            errors.add(new ImportError(row, "Country not found: " + dto.getCountry()));
    }

    public List<AutoCompleteResponse> findAllCustomerByNameIsActive(String name, Boolean isActive) throws ImsBusinessException {
        List<CustomerMaster> customerList = null;
        if (StringUtils.isEmpty(name)) {
            customerList = customerRepository.findTop15ByIsActiveOrderByCustomerNameAsc(isActive);
        } else {
            customerList = customerRepository.findByIsActiveAndCustomerNameIgnoreCaseContaining(isActive, name);
        }
        if (!ObjectUtils.isEmpty(customerList)) {
            return customerList.stream().map(obj -> {
                AutoCompleteResponse resp = new AutoCompleteResponse();
                resp.setId(obj.getId());
                resp.setName(obj.getCustomerName());
                resp.setOption(obj.getId());
                return resp;
            }).toList();
        } else {
            log.info("CustomerServiceImpl::findAllCustomerByNameIsActive:: search product data not found.");
            throw new ImsBusinessException("CUSTOO1", "Customer not found.");
        }
    }

    public CustomerRequest loadCustomer(LoadRequest loadRequest, HttpServletRequest request) throws ImsBusinessException {
        CustomerMaster customerTran = customerRepository.findByIdAndIsActive(loadRequest.getRecordCode(), true);
        if (ObjectUtils.isNotEmpty(customerTran)) {
            return mapperDto(customerTran);
        } else {
            throw new ImsBusinessException("Sale01", "Sale not found for id :"+loadRequest.getRecordCode());
        }
    }

    private CustomerRequest mapperDto(CustomerMaster customerTran) {
        CustomerRequest customer = new CustomerRequest();
        customer.setCustomerName(customerTran.getCustomerName());
        customer.setPhoneNumber(customerTran.getPhoneNumber());
        customer.setEmail(customerTran.getEmail());

        City cityMaster = cityRepository.findByIdAndIsActive(customerTran.getCity().getId(), true);
        customer.setCityId(cityMaster.getId());

        State stateMaster = stateRepository.findByIdAndIsActive(customerTran.getState().getId(), true);
        customer.setStateId(stateMaster.getId());

        Country CountryMaster = countryRepository.findByIdAndIsActive(customerTran.getCountry().getId(), true);
        customer.setCountryId(CountryMaster.getId());

        return customer;
    }


    public CustomerResponse editCustomer( CustomerRequest dto) throws Exception {
        log.info("BranchMasterService::Edit branch request :{}", dto);
        try {
            CustomerMaster cutomerMaster = loadCustomerByName(dto.getCustomerName());
            customerMapper(cutomerMaster, dto);
            cutomerMaster = customerRepository.save(cutomerMaster);
            log.info("customerService::addCustomer:Customer edit successfully.");
            return createResponse(cutomerMaster, "Edit");
        } catch (Exception e) {
            log.error("BranchMasterService::editBranch::Exception occurred in edit Branch for name :{}",
                    dto.getCustomerName(), e);
            throw new ImsBusinessException(BRANCH_EDIT_EXCEPTION_CODE, BRANCH_EDIT_EXCEPTION_MSG);
        }
    }

    private CustomerMaster loadCustomerByName(String customerName) throws ImsBusinessException {
        log.info("CustomerMasterService::loadCustomerByName:Load customer called.");
        Optional<CustomerMaster> customerMasterObj = customerRepository.findByCustomerName(customerName);
        if (customerMasterObj.isPresent() && ObjectUtils.isNotEmpty(customerMasterObj.get())) {
            log.info("CustomerMasterService::loadCustomerByName:customer found.");
            return customerMasterObj.get();
        } else {
            throw new ImsBusinessException(BRANCH_NOT_FOUND_CODE, BRANCH_NOT_FOUND_MSG);
        }
    }

    private void customerMapper(CustomerMaster customerMaster, CustomerRequest dto) {
        customerMaster.setCustomerName(dto.getCustomerName());
        customerMaster.setPhoneNumber(dto.getPhoneNumber());
        customerMaster.setEmail(dto.getEmail());
        customerMaster.setCity(cityRepository.findById(dto.getCityId()).orElse(null));
        customerMaster.setCountry(countryRepository.findById(dto.getCountryId()).orElse(null));
        customerMaster.setState(stateRepository.findById(dto.getStateId()).orElse(null));
    }

    private CustomerResponse createResponse(CustomerMaster customerMaster, String method) throws ImsBusinessException {
        if (ObjectUtils.isNotEmpty(customerMaster)) {
            CustomerResponse resp = new CustomerResponse();
            resp.setCustomerName(customerMaster.getCustomerName());
            resp.setStatus(SUCCESS);
            resp.setMessage("User " + method + "successfully.");
            return resp;
        } else {
            throw new ImsBusinessException(USER_NOT_FOUND_CODE,
                    "User not " + method + "successfully.");
        }
    }
}
