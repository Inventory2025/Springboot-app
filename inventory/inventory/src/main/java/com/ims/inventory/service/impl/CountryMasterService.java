package com.ims.inventory.service.impl;

import com.ims.inventory.domen.dto.CountryDropDownDto;
import com.ims.inventory.domen.request.CountryRequest;
import com.ims.inventory.domen.request.CustomerRequest;
import com.ims.inventory.domen.request.LoadRequest;
import com.ims.inventory.domen.request.RemoveRequest;
import com.ims.inventory.domen.response.CountryResponse;
import com.ims.inventory.domen.response.CustomerResponse;
import com.ims.inventory.exception.ImsBusinessException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface CountryMasterService {
    List<CountryResponse> findAllCountryByIsActive(Boolean isActive) throws ImsBusinessException;

    public CountryResponse addCountry(CountryRequest countryRequest) throws Exception;

    public CountryResponse editCountry(CountryRequest countryRequest) throws Exception;

    CountryResponse CountryDelete(RemoveRequest removeRequest) throws Exception;

    public CountryResponse load(LoadRequest loadRequest, HttpServletRequest request) throws ImsBusinessException;

    public List<CountryDropDownDto> findAllCountry(CountryRequest countryRequest) throws ImsBusinessException;
}
