package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.CustomerMaster;
import com.ims.inventory.domen.entity.RoleMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerMaster, String>, PageableJdbcRepository{
    //Optional<CustomerMaster> findBycustomer_Name(String customer_Name);
}
