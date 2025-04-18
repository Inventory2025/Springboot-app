package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.BranchMaster;
import com.ims.inventory.domen.entity.CustomerMaster;
import com.ims.inventory.domen.entity.RoleMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<BranchMaster, String> {
    Optional<BranchMaster> findByName(String name);

    List<BranchMaster> findByIsActiveAndNameIgnoreCaseContaining(Boolean isActive, String keyword);

    List<BranchMaster> findTop15ByIsActiveOrderByNameAsc(Boolean isActive);
}
