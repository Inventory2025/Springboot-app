package com.ims.inventory.service.impl;

import com.ims.inventory.domen.dto.CityDto;
import com.ims.inventory.domen.entity.City;
import com.ims.inventory.domen.request.CityRequest;
import com.ims.inventory.domen.request.CountryRequest;
import com.ims.inventory.domen.request.LoadRequest;
import com.ims.inventory.domen.request.RemoveRequest;
import com.ims.inventory.domen.response.CityResponse;
import com.ims.inventory.exception.ImsBusinessException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface CityMasterService {

    List<CityResponse> findAllCityByIsActive(Boolean isActive) throws ImsBusinessException;

    public CityResponse addCity(CityRequest cityRequest, HttpServletRequest request) throws Exception;

    public CityResponse editCity(CityRequest cityRequest, HttpServletRequest request) throws Exception;

    CityResponse CityDelete(RemoveRequest removeRequest, HttpServletRequest request) throws Exception;

    public CityResponse load(LoadRequest loadRequest, HttpServletRequest request) throws ImsBusinessException;

    public List<CityDto> findAllCity(CityRequest cityRequest) throws ImsBusinessException;
}
