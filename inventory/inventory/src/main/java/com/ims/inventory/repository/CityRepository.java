package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, String> {

    Optional<City> findByCode(String code);

}
