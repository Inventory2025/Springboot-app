package com.ims.inventory.service;

import com.ims.inventory.domen.entity.UserMaster;
import com.ims.inventory.domen.request.SignUpRequest;
import com.ims.inventory.domen.response.UserDetailResponse;
import com.ims.inventory.exception.ImsBusinessException;

import java.util.List;

public interface UserMasterService {

   List<UserDetailResponse> findAllUserByIsActive(Boolean isActive) throws ImsBusinessException;

   UserMaster addUser(SignUpRequest signUpRequest) throws Exception;
}
