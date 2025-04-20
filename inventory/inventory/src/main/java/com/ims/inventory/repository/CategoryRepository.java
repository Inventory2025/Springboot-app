package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.BranchMaster;
import com.ims.inventory.domen.entity.CategoryMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryMaster, String> {
    Optional<CategoryMaster> findByName(String name);

    CategoryMaster findByIdAndIsActive(String id, boolean isActive);

    @Query("SELECT r FROM CategoryMaster r WHERE r.isActive = :isActive")
    List<CategoryMaster> findAllIsacitve(@Param("isActive") Boolean isActive);
}