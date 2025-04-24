package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.City;
import com.ims.inventory.domen.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StateRepository extends JpaRepository<State, String> {

    Optional<State> findByCode(String code);

    @Query("SELECT r FROM State r WHERE r.isActive = :isActive")
    List<State> findAllIsacitve(@Param("isActive") Boolean isActive);

    State findByIdAndIsActive(String id, boolean isActive);
}
