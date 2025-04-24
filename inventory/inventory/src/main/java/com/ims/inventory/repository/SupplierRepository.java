package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.CustomerMaster;
import com.ims.inventory.domen.entity.SupplierMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupplierRepository extends JpaRepository<SupplierMaster, String>, PageableJdbcRepository {

    Optional<SupplierMaster> findBySupplierName(String supplierName);

    List<SupplierMaster> findAllByIsActive(boolean isActive);

    List<SupplierMaster> findByIsActiveAndSupplierNameIgnoreCaseContaining(Boolean isActive, String keyword);

    List<SupplierMaster> findTop15ByIsActiveOrderBySupplierNameAsc(Boolean isActive);

    SupplierMaster findByIdAndIsActive(String id, boolean isActive);

}
