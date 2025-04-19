package com.ims.inventory.repository.impl;

import com.ims.inventory.domen.entity.City;
import com.ims.inventory.domen.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StateRepository extends JpaRepository<City, String> {

    Optional<State> findByCode(String code);
}
