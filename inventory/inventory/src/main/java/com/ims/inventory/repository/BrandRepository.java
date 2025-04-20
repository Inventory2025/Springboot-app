package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.BradMaster;
import com.ims.inventory.domen.entity.CategoryMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BrandRepository extends JpaRepository<BradMaster, String> {

    @Query("SELECT r FROM BradMaster r WHERE r.isActive = :isActive")
    List<BradMaster> findAllIsacitve(@Param("isActive") Boolean isActive);

    BradMaster findByIdAndIsActive(String id, boolean isActive);
}
