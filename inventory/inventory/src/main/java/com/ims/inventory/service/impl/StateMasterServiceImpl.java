package com.ims.inventory.service.impl;

import com.ims.inventory.domen.dto.CityDto;
import com.ims.inventory.domen.dto.StateDropDowmDto;
import com.ims.inventory.domen.entity.City;
import com.ims.inventory.domen.entity.State;
import com.ims.inventory.domen.request.CityRequest;
import com.ims.inventory.domen.request.LoadRequest;
import com.ims.inventory.domen.request.RemoveRequest;
import com.ims.inventory.domen.request.StateRequest;
import com.ims.inventory.domen.response.CityResponse;
import com.ims.inventory.domen.response.StateResponse;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.repository.CityRepository;
import com.ims.inventory.repository.StateRepository;
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
import static com.ims.inventory.constants.ErrorMsg.CITY_NOT_FOUND_MSG;
import static com.ims.inventory.constants.ImsConstants.SUCCESS;

@Slf4j
@Component("StateMasterServiceImpl")
public class StateMasterServiceImpl implements StateMasterService{

    @Autowired
    private StateRepository stateRepository;

    @Override
    public List<StateResponse> findAllStateByIsActive(Boolean isActive) throws ImsBusinessException {
        log.info("StateMasterServiceImpl::findAllStateByIsActive:: called for isActive :{}", isActive);
        List<State> stateList = stateRepository.findAll();
        if (!ObjectUtils.isEmpty(stateList)) {
            return stateList.stream().map(obj -> {
                StateResponse state = new StateResponse();
                state.setName(obj.getName());
                return state;
            }).toList();
        } else {
            log.info("StateMasterServiceImpl::findAllStateByIsActive:: state data not found.");
            throw new ImsBusinessException("StateOO1", "state not found.");
        }
    }
    private StateResponse createResponse(State state, String method) throws ImsBusinessException {
        if (ObjectUtils.isNotEmpty(state)) {
            StateResponse resp = new StateResponse();
            resp.setName(state.getName());
            return resp;
        } else {
            throw new ImsBusinessException(CATEGORY_NOT_FOUND_CODE,
                    "State not " + method + "successfully.");
        }
    }

    @Override
    public StateResponse addState(StateRequest stateRequest, HttpServletRequest request) throws Exception {
        log.info("StateMasterService::addState request :{}", stateRequest);
        try {
            State state = new State();
            state.setName(state.getName());
            state.setCode(state.getCode());
            StateMapper(state, stateRequest);
            State state1 = stateRepository.save(state);
            log.info("StateMasterService::addState:State save successfully.");
            return createResponse(state1, "Add");
        } catch (Exception e) {
            throw new ImsBusinessException(CITY_ADD_EXCEPTION_CODE, CITY_ADD_EXCEPTION_MSG);
        }
    }

    @Override
    public StateResponse editState(StateRequest stateRequest, HttpServletRequest request) throws Exception {
        log.info("StateMasterService::Edit state request :{}", stateRequest);
        try {
            State stateMaster = loadStateByState(stateRequest.getCode());
            StateMapper(stateMaster, stateRequest);
            stateMaster = stateRepository.save(stateMaster);
            log.info("StateMasterService::editState:State edit successfully.");
            return createResponse(stateMaster, "Edit");
        } catch (Exception e) {
            log.error("StateMasterService::editCity::Exception occurred in edit state for name :{}",
                    stateRequest.getName(), e);
            throw new ImsBusinessException(CITY_EDIT_EXCEPTION_CODE, CITY_EDIT_EXCEPTION_MSG);
        }
    }

    @Override
    public StateResponse stateDelete(RemoveRequest removeRequest, HttpServletRequest request) throws Exception {
        log.info("StateMasterService::StateDelete:: delete state request :{}", removeRequest);
        try {
            State stateMasterObj = loadStateByState(removeRequest.getId());
            stateRepository.delete(stateMasterObj);
            StateResponse resp = new StateResponse();
            resp.setName(removeRequest.getId());
            resp.setStatus(SUCCESS);
            resp.setMessage("State delete successfully.");
            log.info("StateMasterService::addState:State delete successfully.");
            return resp;
        } catch (Exception e) {
            throw new ImsBusinessException(CITY_DELETE_EXCEPTION_CODE, CITY_DELETE_EXCEPTION_MSG);
        }
    }
    private State loadStateByState(String State_Code) throws ImsBusinessException {
        log.info("StateMasterService::loadStateByState:Load state called.");
        Optional<State> StateObj = null;
        if (StateObj.isPresent() && ObjectUtils.isNotEmpty(StateObj.get())) {
            log.info("StateMasterService::loadStateByState:State found.");
            return StateObj.get();
        } else {
            throw new ImsBusinessException(CITY_NOT_FOUND_CODE, CITY_NOT_FOUND_MSG);
        }
    }

    public StateResponse load(LoadRequest loadRequest, HttpServletRequest request) throws ImsBusinessException {
        log.info("StateMasterService::Load:Load  called.");
        Optional<State> StateObj = null;
        if (StateObj.isPresent() && ObjectUtils.isNotEmpty(StateObj.get())) {
            log.info("StateMasterService::load:Load found.");
            State state = StateObj.get();
            return createResponse(state, "load");
        } else {
            throw new ImsBusinessException(CITY_NOT_FOUND_CODE, CITY_NOT_FOUND_MSG);
        }
    }

    private void StateMapper(State state, StateRequest stateRequest) {
        log.info("StateMasterService::StateMapper:State mapper called.");
        state.setCode(state.getCode());
        state.setName(state.getName());
    }

    public List<StateDropDowmDto> findAllState(StateRequest stateRequest) throws ImsBusinessException {
        List<State> stateMasterList = stateRepository.findAllIsacitve(true);

        if (!ObjectUtils.isEmpty(stateMasterList)) {
            return stateMasterList.stream()
                    .map(state -> new StateDropDowmDto(state.getId(), state.getName(), state.getCode()))
                    .collect(Collectors.toList());
        } else {
            log.info("StateMasterServiceImpl::findAllState:: active state not found.");
            throw new ImsBusinessException("CUST001", "Roles not found.");
        }
    }
}
