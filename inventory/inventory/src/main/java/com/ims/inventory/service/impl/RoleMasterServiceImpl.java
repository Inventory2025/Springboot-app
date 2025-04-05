package com.ims.inventory.service.impl;

import com.ims.inventory.domen.entity.RoleMaster;
import com.ims.inventory.domen.request.*;
import com.ims.inventory.domen.response.ApiResponse;
import com.ims.inventory.domen.response.RoleResponse;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.mappers.RoleRowMapper;
import com.ims.inventory.repository.PageableJdbcRepository;
import com.ims.inventory.repository.RoleRepository;
import com.ims.inventory.service.RoleMasterService;
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
@Component("RoleMasterServiceImpl")
public class RoleMasterServiceImpl implements RoleMasterService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public ApiResponse<RoleResponse> findAllRoleByIsActive(FetchRolePageRequest fetchRolePageRequest)
            throws ImsBusinessException{
        log.info("RoleMasterServiceImpl::findAllRoleByIsActive:: called for fetch request :{}", fetchRolePageRequest);

        StringBuilder baseQuery = new StringBuilder("""
            select r.name from tbl_role r
        """);

        ApiResponse<RoleResponse> response = roleRepository.findPageableDataWithFilters(baseQuery.toString(),
                fetchRolePageRequest.getFilters(), fetchRolePageRequest.getPage(), new RoleRowMapper());

        if (!ObjectUtils.isEmpty(response)) {
         return response;
        } else {
            log.info("RoleMasterServiceImpl::findAllRoleByIsActive:: Use data not found.");
            throw new ImsBusinessException("ROO1", "User not found.");
        }
    }


    private RoleResponse createResponse(RoleMaster roleMaster, String method) throws ImsBusinessException {
        if (ObjectUtils.isNotEmpty(roleMaster)) {
            RoleResponse resp = new RoleResponse();
            resp.setName(roleMaster.getName());
            resp.setStatus(SUCCESS);
            resp.setMessage("Role " + method + "successfully.");
            return resp;
        } else {
            throw new ImsBusinessException(ROLE_NOT_FOUND_CODE,
                    "Role not " + method + "successfully.");
        }
    }

    public RoleResponse addRole(RoleRequest roleRequest) throws Exception {
        log.info("RoleMasterService::addRole request :{}", roleRequest);
        try {
            RoleMaster roleMaster = new RoleMaster();
            roleMaster.setName(roleRequest.getName());
            roleMaster.setDescription(roleRequest.getDescription());
            roleMapper(roleMaster, roleRequest);
            RoleMaster role = roleRepository.save(roleMaster);
            log.info("RoleMasterService::addRole:Role save successfully.");
            return createResponse(role, "Add");
        } catch (Exception e) {
            throw new ImsBusinessException(ROLE_ADD_EXCEPTION_CODE, ROLE_ADD_EXCEPTION_MSG);
        }
    }

    public RoleResponse editRole(RoleRequest roleRequest) throws Exception {
        log.info("RoleMasterService::Edit role request :{}", roleRequest);
        try {
            RoleMaster roleMaster = loadRoleByName(roleRequest.getName());
            roleMapper(roleMaster, roleRequest);
            roleMaster = roleRepository.save(roleMaster);
            log.info("RoleMasterService::addRole:Role edit successfully.");
            return createResponse(roleMaster, "Edit");
        } catch (Exception e) {
            log.error("RoleMasterService::editRole::Exception occurred in edit role for name :{}",
                    roleRequest.getName(), e);
            throw new ImsBusinessException(ROLE_EDIT_EXCEPTION_CODE, ROLE_EDIT_EXCEPTION_MSG);
        }
    }

    public RoleResponse roleDelete(RemoveRequest removeRequest) throws Exception {
        log.info("RoleMasterService::roleDelete:: delete role request :{}", removeRequest);
        try {
            RoleMaster roleMasterObj = loadRoleByName(removeRequest.getId());
            roleRepository.delete(roleMasterObj);
            RoleResponse resp = new RoleResponse();
            resp.setName(removeRequest.getId());
            resp.setStatus(SUCCESS);
            resp.setMessage("Role delete successfully.");
            log.info("RoleMasterService::addRole:Role delete successfully.");
            return resp;
        } catch (Exception e) {
            throw new ImsBusinessException(ROLE_DELETE_EXCEPTION_CODE, ROLE_DELETE_EXCEPTION_MSG);
        }
    }

    private RoleMaster loadRoleByName(String name) throws ImsBusinessException {
        log.info("RoleMasterService::loadRoleByName:Load role called.");
        Optional<RoleMaster> roleMasterObj = roleRepository.findByName(name);
        if (roleMasterObj.isPresent() && ObjectUtils.isNotEmpty(roleMasterObj.get())) {
            log.info("RoleMasterService::loadRoleByName:Role found.");
            return roleMasterObj.get() ;
        } else {
            throw new ImsBusinessException(ROLE_NOT_FOUND_CODE, ROLE_NOT_FOUND_MSG);
        }
    }

    private void roleMapper(RoleMaster roleMaster, RoleRequest roleRequest) {
        log.info("RoleMasterService::roleMapper:Role mapper called.");
        roleMaster.setName(roleRequest.getName());
        roleMaster.setDescription(roleRequest.getDescription());
    }




}
