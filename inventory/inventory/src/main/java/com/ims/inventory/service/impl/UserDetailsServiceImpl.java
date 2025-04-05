package com.ims.inventory.service.impl;

import com.ims.inventory.domen.entity.UserMaster;
import com.ims.inventory.repository.UserMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMasterRepository userMasterRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserMaster> userMasterObj = userMasterRepository.findByUsername(username);
        if (userMasterObj.isPresent()) {
            UserMaster userMaster = userMasterObj.get();
            return new User(userMaster.getUsername(), userMaster.getPassword(),
                    new ArrayList<>());
        }
        throw new UsernameNotFoundException("User not found");

    }
}
