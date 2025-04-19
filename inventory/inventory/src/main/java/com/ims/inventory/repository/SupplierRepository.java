package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.SupplierMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<SupplierMaster, String>, PageableJdbcRepository {


    List<SupplierMaster> findAllByIsActive(boolean isActive);

    List<SupplierMaster> findByIsActiveAndSupplierNameIgnoreCaseContaining(Boolean isActive, String keyword);

    List<SupplierMaster> findTop15ByIsActiveOrderBySupplierNameAsc(Boolean isActive);

}
