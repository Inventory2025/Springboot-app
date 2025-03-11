package com.ims.inventory.service.impl;

import com.ims.inventory.domen.entity.UserMaster;
import com.ims.inventory.domen.request.SignUpRequest;
import com.ims.inventory.domen.response.UserDetailResponse;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.repository.UserMasterRepository;
import com.ims.inventory.service.UserMasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

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
                   user.setId(obj.getId());
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

    public UserMaster addUser(SignUpRequest signUpRequest) throws Exception {
        log.info("UserMasterService::addUser signup request :{}", signUpRequest);
        try {
            if(null != signUpRequest) {
                UserMaster userMaster = new UserMaster();
                userMaster.setUsername(signUpRequest.getName());
                userMaster.setEmailId(signUpRequest.getEmail());
                userMaster.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
                return userMasterRepository.save(userMaster);
            } else {
                throw new Exception("Incorrect request for signup user.");
            }
        } catch (Exception e) {
            throw new Exception("Exception occurred for signup user", e);
        }
    }


}
