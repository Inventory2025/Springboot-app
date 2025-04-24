package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.BranchMaster;
import com.ims.inventory.domen.entity.City;
import com.ims.inventory.domen.entity.UnitMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, String> {

    Optional<City> findByCode(String code);

    Optional<City> findById(String id);

    @Query("SELECT r FROM City r WHERE r.isActive = :isActive")
    List<City> findAllIsacitve(@Param("isActive") Boolean isActive);

    City findByIdAndIsActive(String id, boolean isActive);

}
