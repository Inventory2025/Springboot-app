package com.ims.inventory.service.impl;

import com.ims.inventory.domen.entity.UserMaster;
import com.ims.inventory.domen.request.RemoveRequest;
import com.ims.inventory.domen.request.UserRequest;
import com.ims.inventory.domen.response.UserDetailResponse;
import com.ims.inventory.domen.response.UserResponse;
import com.ims.inventory.enums.CrudMethods;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.repository.UserMasterRepository;
import com.ims.inventory.service.UserMasterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.ims.inventory.constants.ErrorCode.*;
import static com.ims.inventory.constants.ErrorMsg.*;
import static com.ims.inventory.constants.ImsConstants.SUCCESS;

@Slf4j
@Component("UserMasterServiceImpl")
public class UserMasterServiceImpl implements UserMasterService {

    @Autowired
    private UserMasterRepository userMasterRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserDetailResponse> findAllUserByIsActive(Boolean isActive) throws ImsBusinessException{
        log.info("UserMasterServiceImpl::findAllUserByIsActive:: called for isActive :{}", isActive);
        List<UserMaster> userList = userMasterRepository.findAll();
        if (!ObjectUtils.isEmpty(userList)) {
           return userList.stream().map( obj -> {
                   UserDetailResponse user = new UserDetailResponse();
                   user.setUsername(obj.getUsername());
                   user.setPassword(obj.getPassword());
                   user.setEmailId(obj.getEmailId());
                   return user;
            }).toList();
        } else {
            log.info("UserMasterServiceImpl::findAllUserByIsActive:: Use data not found.");
            throw new ImsBusinessException("COO1", "User not found.");
        }
    }

    private UserResponse createResponse(UserMaster userMaster, String method) throws ImsBusinessException {
        if (ObjectUtils.isNotEmpty(userMaster)) {
            UserResponse resp = new UserResponse();
            resp.setUserName(userMaster.getUsername());
            resp.setStatus(SUCCESS);
            resp.setMessage("User " + method + "successfully.");
            return resp;
        } else {
            throw new ImsBusinessException(USER_NOT_FOUND_CODE,
                    "User not " + method + "successfully.");
        }
    }

    public UserResponse addUser(UserRequest userRequest) throws Exception {
        log.info("UserMasterService::addUser signup request :{}", userRequest);
        try {
            UserMaster userMaster = new UserMaster();
            userMaster.setUsername(userRequest.getUserName());
            userMaster.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            userMapper(userMaster, userRequest);
            UserMaster user = userMasterRepository.save(userMaster);
            return createResponse(user, "Add");
        } catch (Exception e) {
            throw new ImsBusinessException(USER_ADD_EXCEPTION_CODE, USER_ADD_EXCEPTION_MSG);
        }
    }

    public UserResponse editUser(UserRequest userRequest) throws Exception {
        log.info("UserMasterService::Edit user request :{}", userRequest);
        try {
            UserMaster userMaster = loadUserByUserName(userRequest.getUserName());
            userMapper(userMaster, userRequest);
            userMaster = userMasterRepository.save(userMaster);
            return createResponse(userMaster, "Edit");
        } catch (Exception e) {
            log.error("UserMasterService::editUser::Exception occurred in edit user for username :{}",
                    userRequest.getUserName(), e);
            throw new ImsBusinessException(USER_EDIT_EXCEPTION_CODE, USER_EDIT_EXCEPTION_MSG);
        }
    }

    public UserResponse userDelete(RemoveRequest removeRequest) throws Exception {
        log.info("UserMasterService::userDelete:: delete user request :{}", removeRequest);
        try {
            UserMaster userMasterObj = loadUserByUserName(removeRequest.getId());
            userMasterRepository.delete(userMasterObj);
            UserResponse resp = new UserResponse();
            resp.setUserName(removeRequest.getId());
            resp.setStatus(SUCCESS);
            resp.setMessage("User delete successfully.");
            return resp;
        } catch (Exception e) {
            throw new ImsBusinessException(USER_DELETE_EXCEPTION_CODE, USER_DELETE_EXCEPTION_MSG);
        }
    }

    private UserMaster loadUserByUserName(String userName) throws ImsBusinessException {
        Optional<UserMaster> userMasterObj = userMasterRepository.findByUsername(userName);
        if (userMasterObj.isPresent() && ObjectUtils.isNotEmpty(userMasterObj.get())) {
            return userMasterObj.get() ;
        } else {
            throw new ImsBusinessException(USER_NOT_FOUND_CODE, USER_NOT_FOUND_MSG);
        }
    }

    private void userMapper(UserMaster userMaster, UserRequest userRequest) {
        userMaster.setPhoneNumber(userRequest.getPhoneNumber());
        userMaster.setEmailId(userRequest.getEmail());
        userMaster.setFirstName(userRequest.getFirstName());
        userMaster.setMiddleName(userRequest.getMiddleName());
        userMaster.setLastName(userRequest.getLastName());
        userMaster.setDescription(userRequest.getDescription());
    }




}
