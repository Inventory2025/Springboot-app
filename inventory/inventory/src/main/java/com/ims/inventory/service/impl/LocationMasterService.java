package com.ims.inventory.service.impl;

import com.ims.inventory.domen.request.LocationRequest;
import com.ims.inventory.domen.request.RemoveRequest;
import com.ims.inventory.domen.request.RoleRequest;
import com.ims.inventory.domen.response.LocationResponse;
import com.ims.inventory.domen.response.RoleResponse;
import com.ims.inventory.exception.ImsBusinessException;

import java.util.List;

public interface LocationMasterService {
    List<LocationResponse> findAllLocationByIsActive(Boolean isActive) throws ImsBusinessException;

    public LocationResponse addLocation(LocationRequest locationRequest) throws Exception;

    public LocationResponse editLocation(LocationRequest locationRequest) throws Exception;

    LocationResponse LocationDelete(RemoveRequest removeRequest) throws Exception;
}
