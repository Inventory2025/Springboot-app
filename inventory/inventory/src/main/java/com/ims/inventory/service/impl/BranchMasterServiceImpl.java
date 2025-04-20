package com.ims.inventory.service.impl;

import com.ims.inventory.domen.dto.BranchDto;
import com.ims.inventory.domen.dto.RoleDto;
import com.ims.inventory.domen.entity.BranchMaster;
import com.ims.inventory.domen.entity.CustomerMaster;
import com.ims.inventory.domen.entity.RoleMaster;
import com.ims.inventory.domen.entity.SaleTrans;
import com.ims.inventory.domen.request.*;
import com.ims.inventory.domen.response.AutoCompleteResponse;
import com.ims.inventory.domen.response.BranchResponse;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.repository.BranchRepository;
import com.ims.inventory.utility.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ims.inventory.constants.ErrorCode.*;
import static com.ims.inventory.constants.ErrorMsg.*;
import static com.ims.inventory.constants.ImsConstants.SUCCESS;

@Slf4j
@Component("BranchMasterServiceImpl")
public class BranchMasterServiceImpl implements BranchMasterService {

    @Autowired
    private BranchRepository branchRepository;

    @Override
    public List<BranchResponse> findAllBranchByIsActive(Boolean isActive) throws ImsBusinessException {
        log.info("BranchMasterServiceImpl::findAllBranchByIsActive:: called for isActive :{}", isActive);
        List<BranchMaster> branchList = branchRepository.findAll();
        if (!ObjectUtils.isEmpty(branchList)) {
            return branchList.stream().map(obj -> {
                BranchResponse branch = new BranchResponse();
                branch.setName(obj.getName());
                return branch;
            }).toList();
        } else {
            log.info("BranchMasterServiceImpl::findAllBranchByIsActive:: Branch data not found.");
            throw new ImsBusinessException("BrOO1", "Branch not found.");
        }
    }

    private BranchResponse createResponse(BranchMaster branchMaster, String method) throws ImsBusinessException {
        if (ObjectUtils.isNotEmpty(branchMaster)) {
            BranchResponse resp = new BranchResponse();
            resp.setName(branchMaster.getName());
            resp.setStatus(SUCCESS);
            resp.setMessage("Branch " + method + "successfully.");
            return resp;
        } else {
            throw new ImsBusinessException(BRANCH_NOT_FOUND_CODE,
                    "Branch not " + method + "successfully.");
        }
    }

    @Override
    public BranchResponse addBranch(BranchRequest branchRequest) throws Exception {
        log.info("BranchMasterService::addBranch request :{}", branchRequest);
        try {
            BranchMaster branchMaster = new BranchMaster();
            branchMapper(branchMaster, branchRequest);
            branchMaster.setActive(true);
            BranchMaster branch = branchRepository.save(branchMaster);
            log.info("BranchMasterService::addBranch:Branch save successfully.");
            return createResponse(branch, "Add");
        } catch (Exception e) {
            throw new ImsBusinessException(BRANCH_ADD_EXCEPTION_CODE, BRANCH_ADD_EXCEPTION_MSG);
        }
    }

    @Override
    public BranchResponse editBranch(BranchRequest branchRequest) throws Exception {
        log.info("BranchMasterService::Edit branch request :{}", branchRequest);
        try {
            BranchMaster branchMaster = loadBranchByName(branchRequest.getCode());
            branchMapper(branchMaster, branchRequest);
            branchMaster = branchRepository.save(branchMaster);
            log.info("BranchService::addBranch:Branch edit successfully.");
            return createResponse(branchMaster, "Edit");
        } catch (Exception e) {
            log.error("BranchMasterService::editBranch::Exception occurred in edit Branch for name :{}",
                    branchRequest.getName(), e);
            throw new ImsBusinessException(BRANCH_EDIT_EXCEPTION_CODE, BRANCH_EDIT_EXCEPTION_MSG);
        }
    }


    @Override
    public BranchResponse BranchDelete(RemoveRequest removeRequest) throws Exception {
        log.info("BranchMasterService::BranchDelete:: delete Branch request :{}", removeRequest);
        try {
            BranchMaster branchMasterObj = loadBranchByName(removeRequest.getId());
            branchRepository.delete(branchMasterObj);
            BranchResponse resp = new BranchResponse();
            resp.setName(removeRequest.getId());
            resp.setStatus(SUCCESS);
            resp.setMessage("Branch delete successfully.");
            log.info("BranchMasterService::addBranch:Branch delete successfully.");
            return resp;
        } catch (Exception e) {
            throw new ImsBusinessException(BRANCH_DELETE_EXCEPTION_CODE, BRANCH_DELETE_EXCEPTION_MSG);
        }
    }

    private BranchMaster loadBranchByName(String code) throws ImsBusinessException {
        log.info("BranchMasterService::loadBranchByName:Load branch called.");
        Optional<BranchMaster> branchMasterObj = branchRepository.findByCode(code);
        if (branchMasterObj.isPresent() && ObjectUtils.isNotEmpty(branchMasterObj.get())) {
            log.info("BranchMasterService::loadBranchByName:Branch found.");
            return branchMasterObj.get();
        } else {
            throw new ImsBusinessException(BRANCH_NOT_FOUND_CODE, BRANCH_NOT_FOUND_MSG);
        }
    }

    private void branchMapper(BranchMaster branchMaster, BranchRequest branchRequest) {
        log.info("BranchMasterService::branchMapper:Branch mapper called.");
        branchMaster.setName(branchRequest.getName());
        branchMaster.setCode(Util.generateCustomId());
        branchMaster.setAddress(branchRequest.getAddress());
        branchMaster.setDescription(branchRequest.getDescription());
    }

    public List<AutoCompleteResponse> findAllBranchByNameIsActive(String name, Boolean isActive) throws ImsBusinessException {
        List<BranchMaster> branchList = null;
        if (StringUtils.isEmpty(name)) {
            branchList = branchRepository.findTop15ByIsActiveOrderByNameAsc(isActive);
        } else {
            branchList = branchRepository.findByIsActiveAndNameIgnoreCaseContaining(isActive, name);
        }
        if (!ObjectUtils.isEmpty(branchList)) {
            return branchList.stream().map(obj -> {
                AutoCompleteResponse resp = new AutoCompleteResponse();
                resp.setId(obj.getId());
                resp.setName(obj.getName());
                resp.setOption(obj.getCode());
                return resp;
            }).toList();
        } else {
            log.info("BranchMasterServiceImpl::findAllBranchByNameIsActive:: Search branch data not found.");
            throw new ImsBusinessException("BRANCHOO1", "Branch not found.");
        }
    }

    public BranchRequest loadBranch(LoadRequest loadRequest) throws ImsBusinessException {
        BranchMaster branchTran = branchRepository.findByIdAndIsActive(loadRequest.getRecordCode(), true);
        if (ObjectUtils.isNotEmpty(branchTran)) {
            return mapperDto(branchTran);
        } else {
            throw new ImsBusinessException("Sale01", "Sale not found for id :"+loadRequest.getRecordCode());
        }
    }

    private BranchRequest mapperDto(BranchMaster branchTran) {
        BranchRequest branch = new BranchRequest();
        branch.setCode(branchTran.getCode());
        branch.setName(branchTran.getName());
        branch.setAddress(branchTran.getAddress());
        branch.setDescription(branchTran.getDescription());
        return branch;
    }

    public List<BranchDto> findAllBranch(BranchRequest branchRequest) throws ImsBusinessException {
        List<BranchMaster> branchMasterList = branchRepository.findAllIsacitve(true);

        if (!ObjectUtils.isEmpty(branchMasterList)) {
            return branchMasterList.stream()
                    .map(role -> new BranchDto(role.getId(), role.getName(), role.getCode()))
                    .collect(Collectors.toList());
        } else {
            log.info("CustomerServiceImpl::findAllRole:: active roles not found.");
            throw new ImsBusinessException("CUST001", "Roles not found.");
        }
    }
}
