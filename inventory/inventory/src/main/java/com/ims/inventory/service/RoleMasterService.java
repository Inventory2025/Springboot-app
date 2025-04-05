package com.ims.inventory.service;

import com.ims.inventory.domen.request.FetchRolePageRequest;
import com.ims.inventory.domen.request.RemoveRequest;
import com.ims.inventory.domen.request.RoleRequest;
import com.ims.inventory.domen.response.ApiResponse;
import com.ims.inventory.domen.response.RoleResponse;
import com.ims.inventory.exception.ImsBusinessException;

public interface RoleMasterService {
    ApiResponse<RoleResponse> findAllRoleByIsActive(FetchRolePageRequest fetchRolePageRequest) throws ImsBusinessException;

    public RoleResponse addRole(RoleRequest roleRequest) throws Exception;

    public RoleResponse editRole(RoleRequest roleRequest) throws Exception;

    RoleResponse roleDelete(RemoveRequest removeRequest) throws Exception;
}
