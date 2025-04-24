package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.City;
import com.ims.inventory.domen.entity.Country;
import com.ims.inventory.domen.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, String> {

    Optional<Country> findByCode(String code);

    @Query("SELECT r FROM Country r WHERE r.isActive = :isActive")
    List<Country> findAllIsacitve(@Param("isActive") Boolean isActive);

    Country findByIdAndIsActive(String id, boolean isActive);
}
