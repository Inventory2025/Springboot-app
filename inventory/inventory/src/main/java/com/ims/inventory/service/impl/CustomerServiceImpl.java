package com.ims.inventory.service.impl;

import com.ims.inventory.domen.dto.CustomerImportDto;
import com.ims.inventory.domen.entity.CustomerMaster;
import com.ims.inventory.domen.entity.ProductMaster;
import com.ims.inventory.domen.request.CustomerRequest;
import com.ims.inventory.domen.response.AutoCompleteResponse;
import com.ims.inventory.domen.response.CustomerResponse;
import com.ims.inventory.exception.ImportError;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.repository.CityRepository;
import com.ims.inventory.repository.CountryRepository;
import com.ims.inventory.repository.CustomerRepository;
import com.ims.inventory.repository.StateRepository;
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
import java.util.Set;

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
        customer.setAddressLine1(dto.getAddressLine1());
        customer.setAddressLine2(dto.getAddressLine2());
        customer.setAddressLine3(dto.getAddressLine3());
        customer.setPinCode(dto.getPinCode());
        customer.setActive(true);

        customer.setCity(cityRepository.findByCode(dto.getCity()).orElse(null));
        customer.setState(stateRepository.findByCode(dto.getState()).orElse(null));
        customer.setCountry(countryRepository.findByCode(dto.getCountry()).orElse(null));

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
        customer.setAddressLine1(dto.getAddressLine1());
        customer.setAddressLine2(dto.getAddressLine2());
        customer.setAddressLine3(dto.getAddressLine3());
        customer.setPinCode(dto.getPinCode());
        customer.setActive(true);

        customer.setCity(cityRepository.findByCode(dto.getCity()).orElse(null));
        customer.setState(stateRepository.findByCode(dto.getState()).orElse(null));
        customer.setCountry(countryRepository.findByCode(dto.getCountry()).orElse(null));

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

}
