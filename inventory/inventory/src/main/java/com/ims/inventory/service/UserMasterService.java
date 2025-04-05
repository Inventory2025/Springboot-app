package com.ims.inventory.service;

import com.ims.inventory.domen.entity.UserMaster;
import com.ims.inventory.domen.request.RemoveRequest;
import com.ims.inventory.domen.request.UserRequest;
import com.ims.inventory.domen.response.UserDetailResponse;
import com.ims.inventory.domen.response.UserResponse;
import com.ims.inventory.exception.ImsBusinessException;

import java.util.List;

public interface UserMasterService {

   List<UserDetailResponse> findAllUserByIsActive(Boolean isActive) throws ImsBusinessException;

   public UserResponse addUser(UserRequest userRequest) throws Exception;

   public UserResponse editUser(UserRequest userRequest) throws Exception;

   UserResponse userDelete(RemoveRequest removeRequest) throws Exception;
}
