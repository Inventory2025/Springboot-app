package com.ims.inventory.service.impl;

import com.ims.inventory.domen.entity.City;
import com.ims.inventory.domen.entity.Country;
import com.ims.inventory.domen.request.CityRequest;
import com.ims.inventory.domen.request.CountryRequest;
import com.ims.inventory.domen.request.LoadRequest;
import com.ims.inventory.domen.request.RemoveRequest;
import com.ims.inventory.domen.response.CityResponse;
import com.ims.inventory.domen.response.CountryResponse;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.repository.CityRepository;
import com.ims.inventory.repository.CountryRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.ims.inventory.constants.ErrorCode.*;
import static com.ims.inventory.constants.ErrorMsg.*;
import static com.ims.inventory.constants.ErrorMsg.CATEGORY_NOT_FOUND_MSG;
import static com.ims.inventory.constants.ImsConstants.SUCCESS;

@Slf4j
@Component("CityMasterServiceImpl")
public class CityMasterServiceImpl implements CityMasterService{


    @Autowired
    private CityRepository cityRepository;

    @Override
    public List<CityResponse> findAllCityByIsActive(Boolean isActive) throws ImsBusinessException {
        log.info("CityMasterServiceImpl::findAllCityByIsActive:: called for isActive :{}", isActive);
        List<City> cityList = cityRepository.findAll();
        if (!ObjectUtils.isEmpty(cityList)) {
            return cityList.stream().map(obj -> {
                CityResponse city = new CityResponse();
                city.setName(obj.getName());
                return city;
            }).toList();
        } else {
            log.info("CityMasterServiceImpl::findAllCityByIsActive:: city data not found.");
            throw new ImsBusinessException("CityOO1", "city not found.");
        }
    }
    private CityResponse createResponse(City city, String method) throws ImsBusinessException {
        if (ObjectUtils.isNotEmpty(city)) {
            CityResponse resp = new CityResponse();
            resp.setName(city.getName());
            return resp;
        } else {
            throw new ImsBusinessException(CATEGORY_NOT_FOUND_CODE,
                    "Country not " + method + "successfully.");
        }
    }

    @Override
    public CityResponse addCity(CityRequest cityRequest, HttpServletRequest request) throws Exception {
        log.info("CityMasterService::addCity request :{}", cityRequest);
        try {
            City cityMaster = new City();
            cityMaster.setName(cityMaster.getName());
            cityMaster.setCode(cityMaster.getCode());
            CityMapper(cityMaster, cityRequest);
            City city = cityRepository.save(cityMaster);
            log.info("CityMasterService::addCity:City save successfully.");
            return createResponse(city, "Add");
        } catch (Exception e) {
            throw new ImsBusinessException(CITY_ADD_EXCEPTION_CODE, CITY_ADD_EXCEPTION_MSG);
        }
    }

    @Override
    public CityResponse editCity(CityRequest cityRequest, HttpServletRequest request) throws Exception {
        log.info("CityMasterService::Edit city request :{}", cityRequest);
        try {
            City cityMaster = loadCityByCity(cityRequest.getCode());
            CityMapper(cityMaster, cityRequest);
            cityMaster = cityRepository.save(cityMaster);
            log.info("CityMasterService::editCity:City edit successfully.");
            return createResponse(cityMaster, "Edit");
        } catch (Exception e) {
            log.error("CityMasterService::editCity::Exception occurred in edit city for name :{}",
                    cityRequest.getName(), e);
            throw new ImsBusinessException(CITY_EDIT_EXCEPTION_CODE, CITY_EDIT_EXCEPTION_MSG);
        }
    }

    @Override
    public CityResponse CityDelete(RemoveRequest removeRequest, HttpServletRequest request) throws Exception {
        log.info("CityMasterService::CityDelete:: delete country request :{}", removeRequest);
        try {
            City cityMasterObj = loadCityByCity(removeRequest.getId());
            cityRepository.delete(cityMasterObj);
            CityResponse resp = new CityResponse();
            resp.setName(removeRequest.getId());
            resp.setStatus(SUCCESS);
            resp.setMessage("City delete successfully.");
            log.info("CityMasterService::addCity:City delete successfully.");
            return resp;
        } catch (Exception e) {
            throw new ImsBusinessException(CITY_DELETE_EXCEPTION_CODE, CITY_DELETE_EXCEPTION_MSG);
        }
    }
    private City loadCityByCity(String CityId) throws ImsBusinessException {
        log.info("CityMasterService::loadCityByCity:Load city called.");
        Optional<City> CityObj = null;
        if (CityObj.isPresent() && ObjectUtils.isNotEmpty(CityObj.get())) {
            log.info("CountryMasterService::loadCountryByCountry:Country found.");
            return CityObj.get();
        } else {
            throw new ImsBusinessException(CITY_NOT_FOUND_CODE, CITY_NOT_FOUND_MSG);
        }
    }

    public CityResponse load(LoadRequest loadRequest, HttpServletRequest request) throws ImsBusinessException {
        log.info("CityMasterService::loadCityByCity:Load city called.");
        Optional<City> cityObj = null;
        if (cityObj.isPresent() && ObjectUtils.isNotEmpty(cityObj.get())) {
            log.info("CountryMasterService::loadCountryByCountry:Country found.");
            City city = cityObj.get();
            return createResponse(city, "load");
        } else {
            throw new ImsBusinessException(CITY_NOT_FOUND_CODE, CITY_NOT_FOUND_MSG);
        }
    }
    private void CityMapper(City city, CityRequest cityRequest) {
        log.info("CityMasterService::CityMapper:City mapper called.");
        city.setCode(city.getCode());
        city.setName(city.getName());
    }
}
