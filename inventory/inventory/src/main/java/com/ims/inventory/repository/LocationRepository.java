package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.LocationMaster;
import com.ims.inventory.domen.entity.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<LocationMaster, String> {
    Optional<LocationMaster> findBypin(String pin);
}
