package com.ims.inventory.service.impl;

import com.ims.inventory.domen.entity.BranchMaster;
import com.ims.inventory.domen.request.BranchRequest;
import com.ims.inventory.domen.request.RemoveRequest;
import com.ims.inventory.domen.response.BranchResponse;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.repository.BranchRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
            branchMaster.setName(branchMaster.getName());
            branchMaster.setDescription(branchMaster.getDescription());
            branchMapper(branchMaster, branchRequest);
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
            BranchMaster branchMaster = loadBranchByName(branchRequest.getName());
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

    private BranchMaster loadBranchByName(String name) throws ImsBusinessException {
        log.info("BranchMasterService::loadBranchByName:Load branch called.");
        Optional<BranchMaster> branchMasterObj = branchRepository.findByName(name);
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
        branchMaster.setDescription(branchRequest.getDescription());
    }
}
