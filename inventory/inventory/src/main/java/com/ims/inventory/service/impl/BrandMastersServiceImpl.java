package com.ims.inventory.service.impl;

import com.ims.inventory.domen.dto.BranchDto;
import com.ims.inventory.domen.dto.BrandDropDownDto;
import com.ims.inventory.domen.entity.BradMaster;
import com.ims.inventory.domen.entity.BranchMaster;
import com.ims.inventory.domen.request.BranchRequest;
import com.ims.inventory.domen.request.BrandRequest;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.repository.BranchRepository;
import com.ims.inventory.repository.BrandRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component("BrandMastersServiceImpl")
public class BrandMastersServiceImpl implements BrandMasterService {
    @Autowired
    private BrandRepository brandRepository;

    public List<BrandDropDownDto> findAllBrand(BrandRequest brandRequest) throws ImsBusinessException {
        List<BradMaster> brandMasterList = brandRepository.findAllIsacitve(true);

        if (!ObjectUtils.isEmpty(brandMasterList)) {
            return brandMasterList.stream()
                    .map(brand -> new BrandDropDownDto(brand.getId(), brand.getName(), brand.getCode()))
                    .collect(Collectors.toList());
        } else {
            log.info("CustomerServiceImpl::findAllRole:: active roles not found.");
            throw new ImsBusinessException("CUST001", "Roles not found.");
        }
    }
}
