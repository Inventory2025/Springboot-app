package com.ims.inventory.service.impl;

import com.ims.inventory.domen.dto.StateDropDowmDto;
import com.ims.inventory.domen.request.CityRequest;
import com.ims.inventory.domen.request.LoadRequest;
import com.ims.inventory.domen.request.RemoveRequest;
import com.ims.inventory.domen.request.StateRequest;
import com.ims.inventory.domen.response.CityResponse;
import com.ims.inventory.domen.response.StateResponse;
import com.ims.inventory.exception.ImsBusinessException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface StateMasterService {


    List<StateResponse> findAllStateByIsActive(Boolean isActive) throws ImsBusinessException;

    public StateResponse addState(StateRequest stateRequest, HttpServletRequest request) throws Exception;

    public StateResponse editState(StateRequest stateRequest, HttpServletRequest request) throws Exception;

    public StateResponse stateDelete(RemoveRequest removeRequest, HttpServletRequest request) throws Exception;

    public StateResponse load(LoadRequest loadRequest, HttpServletRequest request) throws ImsBusinessException;

    public List<StateDropDowmDto> findAllState(StateRequest stateRequest) throws ImsBusinessException;

}
