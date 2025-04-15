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

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMasterRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserMaster userMaster = userRepository.findByUsername(username);
        if (userMaster == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new User(userMaster.getUsername(), userMaster.getPassword(),
                new ArrayList<>());
    }
}
