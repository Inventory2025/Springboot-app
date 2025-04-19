package com.ims.inventory.service.impl;

import com.ims.inventory.domen.dto.BranchDto;
import com.ims.inventory.domen.request.BranchRequest;
import com.ims.inventory.domen.request.LoadRequest;
import com.ims.inventory.domen.request.RemoveRequest;
import com.ims.inventory.domen.request.RoleRequest;
import com.ims.inventory.domen.response.AutoCompleteResponse;
import com.ims.inventory.domen.response.BranchResponse;
import com.ims.inventory.domen.response.RoleResponse;
import com.ims.inventory.exception.ImsBusinessException;

import java.util.List;

public interface BranchMasterService {
    List<BranchResponse> findAllBranchByIsActive(Boolean isActive) throws ImsBusinessException;

    public BranchResponse addBranch(BranchRequest roleRequest) throws Exception;

    public BranchResponse editBranch(BranchRequest roleRequest) throws Exception;

    BranchResponse BranchDelete(RemoveRequest removeRequest) throws Exception;

    List<AutoCompleteResponse> findAllBranchByNameIsActive(String name, Boolean isActive) throws ImsBusinessException;

    public BranchRequest loadBranch(LoadRequest loadRequest) throws ImsBusinessException;

    public List<BranchDto> findAllBranch(BranchRequest branchRequest) throws ImsBusinessException;
}
