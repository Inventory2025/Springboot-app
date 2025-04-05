package com.ims.inventory.service.impl;

import com.ims.inventory.domen.entity.BranchMaster;
import com.ims.inventory.domen.entity.LocationMaster;
import com.ims.inventory.domen.request.BranchRequest;
import com.ims.inventory.domen.request.LocationRequest;
import com.ims.inventory.domen.request.RemoveRequest;
import com.ims.inventory.domen.response.BranchResponse;
import com.ims.inventory.domen.response.LocationResponse;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.repository.BranchRepository;
import com.ims.inventory.repository.LocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.ims.inventory.constants.ErrorCode.*;
import static com.ims.inventory.constants.ErrorMsg.*;
import static com.ims.inventory.constants.ErrorMsg.BRANCH_NOT_FOUND_MSG;
import static com.ims.inventory.constants.ImsConstants.SUCCESS;

@Slf4j
@Component("LocationMasterServiceImpl")
public class LocationMasterServiceImpl implements LocationMasterService{
    @Autowired
    private LocationRepository locationRepository;

    @Override
    public List<LocationResponse> findAllLocationByIsActive(Boolean isActive) throws ImsBusinessException {
        log.info("LocationMasterServiceImpl::findAllLocationByIsActive:: called for isActive :{}", isActive);
        List<LocationMaster> locationList = locationRepository.findAll();
        if (!ObjectUtils.isEmpty(locationList)) {
            return locationList.stream().map(obj -> {
                LocationResponse loc = new LocationResponse();
                loc.setPin(obj.getPin());
                return loc;
            }).toList();
        } else {
            log.info("LocationMasterServiceImpl::findAllLocationByIsActive:: Location data not found.");
            throw new ImsBusinessException("LocOO1", "Location not found.");
        }
    }

    private LocationResponse createResponse(LocationMaster locationMaster, String method) throws ImsBusinessException {
        if (ObjectUtils.isNotEmpty(locationMaster)) {
            LocationResponse resp = new LocationResponse();
            resp.setPin(locationMaster.getPin());
            resp.setStatus(SUCCESS);
            resp.setMessage("Location " + method + "successfully.");
            return resp;
        } else {
            throw new ImsBusinessException(LOCATION_NOT_FOUND_CODE,
                    "Location not " + method + "successfully.");
        }
    }

    @Override
    public LocationResponse addLocation(LocationRequest locationRequest) throws Exception {
        log.info("LocationMasterService::addLocation request :{}", locationRequest);
        try {
            LocationMaster locationMaster = new LocationMaster();
            locationMaster.setPin(locationMaster.getPin());
            locationMaster.setCity(locationMaster.getCity());
            locationMapper(locationMaster, locationRequest);
            LocationMaster location = locationRepository.save(locationMaster);
            log.info("LocationMasterService::addLocation:Location save successfully.");
            return createResponse(location, "Add");
        } catch (Exception e) {
            throw new ImsBusinessException(LOCATION_ADD_EXCEPTION_CODE, LOCATION_ADD_EXCEPTION_MSG);
        }
    }

    @Override
    public LocationResponse editLocation(LocationRequest locationRequest) throws Exception {
        log.info("LocationMasterService::Edit location request :{}", locationRequest);
        try {
            LocationMaster locationMaster = loadLocationByPin(locationRequest.getPin());
            locationMapper(locationMaster, locationRequest);
            locationMaster = locationRepository.save(locationMaster);
            log.info("LocationService::addLocation:Location edit successfully.");
            return createResponse(locationMaster, "Edit");
        } catch (Exception e) {
            log.error("LocationMasterService::editLocation::Exception occurred in edit Location for name :{}",
                    locationRequest.getPin(), e);
            throw new ImsBusinessException(LOCATION_EDIT_EXCEPTION_CODE, LOCATION_EDIT_EXCEPTION_MSG);
        }
    }


    @Override
    public LocationResponse LocationDelete(RemoveRequest removeRequest) throws Exception {
        log.info("LocationMasterService::locationDelete:: delete location request :{}", removeRequest);
        try {
            LocationMaster locationMasterObj = loadLocationByPin(removeRequest.getId());
            locationRepository.delete(locationMasterObj);
            LocationResponse resp = new LocationResponse();
            resp.setPin(removeRequest.getId());
            resp.setStatus(SUCCESS);
            resp.setMessage("Location delete successfully.");
            log.info("LocationMasterService::addLocation:Location delete successfully.");
            return resp;
        } catch (Exception e) {
            throw new ImsBusinessException(LOCATION_DELETE_EXCEPTION_CODE, LOCATION_DELETE_EXCEPTION_MSG);
        }
    }

    private LocationMaster loadLocationByPin(String pin) throws ImsBusinessException {
        log.info("LocationMasterService::loadLocationByPin:Load location called.");
        Optional<LocationMaster> locationMasterObj = locationRepository.findBypin(pin);
        if (locationMasterObj.isPresent() && ObjectUtils.isNotEmpty(locationMasterObj.get())) {
            log.info("LocationMasterService::loadLocationByPin:Location found.");
            return locationMasterObj.get();
        } else {
            throw new ImsBusinessException(LOCATION_NOT_FOUND_CODE, LOCATION_NOT_FOUND_MSG);
        }
    }

    private void locationMapper(LocationMaster locationMaster, LocationRequest locationRequest) {
        log.info("LocationMasterService::locationMapper:Location mapper called.");
        locationMaster.setPin(locationRequest.getPin());
        locationMaster.setCity(locationRequest.getCity());
    }
}
