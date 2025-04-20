package com.ims.inventory.service.impl;

import com.ims.inventory.domen.dto.BrandDropDownDto;
import com.ims.inventory.domen.request.BrandRequest;
import com.ims.inventory.exception.ImsBusinessException;

import java.util.List;

public interface BrandMasterService {

    public List<BrandDropDownDto> findAllBrand(BrandRequest brandRequest) throws ImsBusinessException;
}
