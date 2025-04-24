package com.ims.inventory.service.impl;

import com.ims.inventory.domen.dto.CityDto;
import com.ims.inventory.domen.dto.CountryDropDownDto;
import com.ims.inventory.domen.entity.City;
import com.ims.inventory.domen.entity.Country;
import com.ims.inventory.domen.entity.CustomerMaster;
import com.ims.inventory.domen.entity.RoleMaster;
import com.ims.inventory.domen.request.*;
import com.ims.inventory.domen.response.CityResponse;
import com.ims.inventory.domen.response.CountryResponse;
import com.ims.inventory.domen.response.CustomerResponse;
import com.ims.inventory.domen.response.RoleResponse;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.repository.CountryRepository;
import com.ims.inventory.repository.CustomerRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ims.inventory.constants.ErrorCode.*;
import static com.ims.inventory.constants.ErrorMsg.*;
import static com.ims.inventory.constants.ImsConstants.SUCCESS;

@Slf4j
@Component("CountryMasterServiceImpl")
public class CountryMasterServiceImpl implements CountryMasterService{

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public List<CountryResponse> findAllCountryByIsActive(Boolean isActive) throws ImsBusinessException {
        log.info("CountryMasterServiceImpl::findAllCountryByIsActive:: called for isActive :{}", isActive);
        List<Country> countryList = countryRepository.findAll();
        if (!ObjectUtils.isEmpty(countryList)) {
            return countryList.stream().map(obj -> {
                CountryResponse country = new CountryResponse();
                country.setName(obj.getName());
                return country;
            }).toList();
        } else {
            log.info("CountryMasterServiceImpl::findAllCountryByIsActive:: customer data not found.");
            throw new ImsBusinessException("ContryOO1", "contry not found.");
        }
    }
    private CountryResponse createResponse(Country contry, String method) throws ImsBusinessException {
        if (ObjectUtils.isNotEmpty(contry)) {
            CountryResponse resp = new CountryResponse();
            resp.setName(contry.getName());
            return resp;
        } else {
            throw new ImsBusinessException(CATEGORY_NOT_FOUND_CODE,
                    "Country not " + method + "successfully.");
        }
    }

    @Override
    public CountryResponse addCountry(CountryRequest contryRequest) throws Exception {
        log.info("CountryMasterService::addCountry request :{}", contryRequest);
        try {
            Country countryMaster = new Country();
            countryMaster.setName(countryMaster.getName());
            countryMaster.setCode(countryMaster.getCode());
            CountryMapper(countryMaster, contryRequest);
            Country country = countryRepository.save(countryMaster);
            log.info("RoleMasterService::addRole:Role save successfully.");
            return createResponse(country, "Add");
        } catch (Exception e) {
            throw new ImsBusinessException(COUNTRY_ADD_EXCEPTION_CODE, COUNTRY_ADD_EXCEPTION_MSG);
        }
    }

    @Override
    public CountryResponse editCountry(CountryRequest countryRequest) throws Exception {
        log.info("CountryMasterService::Edit country request :{}", countryRequest);
        try {
            Country countryMaster = loadCountryByCountry(countryRequest.getCode());
            CountryMapper(countryMaster, countryRequest);
            countryMaster = countryRepository.save(countryMaster);
            log.info("CountryMasterService::editCountry:Country edit successfully.");
            return createResponse(countryMaster, "Edit");
        } catch (Exception e) {
            log.error("CountryMasterService::editCountry::Exception occurred in edit country for name :{}",
                    countryRequest.getName(), e);
            throw new ImsBusinessException(COUNTRY_EDIT_EXCEPTION_CODE, COUNTRY_EDIT_EXCEPTION_MSG);
        }
    }

    @Override
    public CountryResponse CountryDelete(RemoveRequest removeRequest) throws Exception {
        log.info("CountryMasterService::countryDelete:: delete country request :{}", removeRequest);
        try {
            Country roleMasterObj = loadCountryByCountry(removeRequest.getId());
            countryRepository.delete(roleMasterObj);
            CountryResponse resp = new CountryResponse();
            resp.setName(removeRequest.getId());
            resp.setStatus(SUCCESS);
            resp.setMessage("Country delete successfully.");
            log.info("CountryMasterService::addCountry:Country delete successfully.");
            return resp;
        } catch (Exception e) {
            throw new ImsBusinessException(COUNTRY_DELETE_EXCEPTION_CODE, COUNTRY_DELETE_EXCEPTION_MSG);
        }
    }
    private Country loadCountryByCountry(String Country_Code) throws ImsBusinessException {
        log.info("CountryMasterService::loadCountryByCountry:Load Country called.");
        Optional<Country> countryrObj = null;
        if (countryrObj.isPresent() && ObjectUtils.isNotEmpty(countryrObj.get())) {
            log.info("CountryMasterService::loadCountryByCountry:Country found.");
            return countryrObj.get();
        } else {
            throw new ImsBusinessException(COUNTRY_NOT_FOUND_CODE, COUNTRY_NOT_FOUND_MSG);
        }
    }

    public CountryResponse load(LoadRequest loadRequest, HttpServletRequest request) throws ImsBusinessException {
        log.info("CountryMasterService::Load:Load city called.");
        Optional<Country> countryObj = null;
        if (countryObj.isPresent() && ObjectUtils.isNotEmpty(countryObj.get())) {
            log.info("CountryMasterService::Load:Country found.");
            Country country = countryObj.get();
            return createResponse(country, "load");
        } else {
            throw new ImsBusinessException(CITY_NOT_FOUND_CODE, CITY_NOT_FOUND_MSG);
        }
    }
    private void CountryMapper(Country country, CountryRequest countryRequest) {
        log.info("CountryMasterService::customerMapper:Country mapper called.");
        country.setCode(country.getCode());
        country.setName(country.getName());

    }

    public List<CountryDropDownDto> findAllCountry(CountryRequest countryRequest) throws ImsBusinessException {
        List<Country> countryMasterList = countryRepository.findAllIsacitve(true);

        if (!ObjectUtils.isEmpty(countryMasterList)) {
            return countryMasterList.stream()
                    .map(country -> new CountryDropDownDto(country.getId(), country.getName(), country.getCode()))
                    .collect(Collectors.toList());
        } else {
            log.info("countryMasterServiceImpl::findAllCountry:: active country not found.");
            throw new ImsBusinessException("CUST001", "Roles not found.");
        }
    }
}
