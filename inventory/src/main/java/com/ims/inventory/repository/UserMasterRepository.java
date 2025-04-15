package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMasterRepository extends JpaRepository<UserMaster, Long> {
    UserMaster findByUsername(String username);
}