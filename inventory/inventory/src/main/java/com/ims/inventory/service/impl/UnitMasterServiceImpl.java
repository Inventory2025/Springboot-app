package com.ims.inventory.service.impl;

import com.ims.inventory.domen.dto.BrandDropDownDto;
import com.ims.inventory.domen.dto.UnitDropDownDto;
import com.ims.inventory.domen.entity.BradMaster;
import com.ims.inventory.domen.entity.UnitMaster;
import com.ims.inventory.domen.request.BrandRequest;
import com.ims.inventory.domen.request.UnitRequest;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.repository.UnitRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component("UnitMasterServiceImpl")
public class UnitMasterServiceImpl implements UnitMasterService{
    @Autowired
    UnitRepository unitRepository;

    public List<UnitDropDownDto> findAllUnit(UnitRequest unitRequest) throws ImsBusinessException {
        List<UnitMaster> unitMasterList = unitRepository.findAllIsacitve(true);

        if (!ObjectUtils.isEmpty(unitMasterList)) {
            return unitMasterList.stream()
                    .map(unit -> new UnitDropDownDto(unit.getId(), unit.getName(), unit.getCode()))
                    .collect(Collectors.toList());
        } else {
            log.info("CustomerServiceImpl::findAllRole:: active roles not found.");
            throw new ImsBusinessException("CUST001", "Roles not found.");
        }
    }
}
