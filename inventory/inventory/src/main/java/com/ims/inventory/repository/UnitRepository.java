package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.BradMaster;
import com.ims.inventory.domen.entity.UnitMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UnitRepository extends JpaRepository<UnitMaster, String> {

    @Query("SELECT r FROM UnitMaster r WHERE r.isActive = :isActive")
    List<UnitMaster> findAllIsacitve(@Param("isActive") Boolean isActive);

    UnitMaster findByIdAndIsActive(String id, boolean isActive);
}
