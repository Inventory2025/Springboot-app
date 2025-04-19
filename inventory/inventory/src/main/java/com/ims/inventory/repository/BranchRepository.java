package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<BranchMaster, String> {

    Optional<BranchMaster> findByName(String name);

    Optional<BranchMaster> findById(String id);

    List<BranchMaster> findByIsActiveAndNameIgnoreCaseContaining(Boolean isActive, String keyword);

    List<BranchMaster> findTop15ByIsActiveOrderByNameAsc(Boolean isActive);

    BranchMaster findByIdAndIsActive(String id, boolean isActive);

    @Query("SELECT r FROM BranchMaster r WHERE r.isActive = :isActive")
    List<BranchMaster> findAllIsacitve(@Param("isActive") Boolean isActive);
}
