package com.ims.inventory.service.impl;

import com.ims.inventory.domen.dto.UnitDropDownDto;
import com.ims.inventory.domen.request.UnitRequest;
import com.ims.inventory.exception.ImsBusinessException;

import java.util.List;

public interface UnitMasterService {

    public List<UnitDropDownDto> findAllUnit(UnitRequest unitRequest) throws ImsBusinessException;
}
