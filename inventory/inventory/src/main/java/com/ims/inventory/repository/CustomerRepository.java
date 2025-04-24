package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.CustomerMaster;
import com.ims.inventory.domen.entity.ProductMaster;
import com.ims.inventory.domen.entity.RoleMaster;
import com.ims.inventory.domen.entity.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerMaster, String>, PageableJdbcRepository{

    //Optional<CustomerMaster> findBycustomer_Name(String customer_Name);


    List<CustomerMaster> findAllByIsActive(boolean isActive);

    List<CustomerMaster> findByIsActiveAndCustomerNameIgnoreCaseContaining(Boolean isActive, String keyword);

    List<CustomerMaster> findTop15ByIsActiveOrderByCustomerNameAsc(Boolean isActive);

    CustomerMaster findByIdAndIsActive(String id, boolean isActive);

    Optional<CustomerMaster> findByCustomerName(String customerName);

}
