package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.UserMaster;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserMasterRepository extends JpaRepository<UserMaster, String> {

    Optional<UserMaster> findByUsername(String username);

}