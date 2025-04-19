package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.CustomerMaster;
import com.ims.inventory.domen.entity.RoleMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleMaster, String>, PageableJdbcRepository  {
     Optional<RoleMaster> findByName(String name);

     Optional<RoleMaster> findById(String id);

     @Query("SELECT r FROM RoleMaster r WHERE r.isActive = :isActive")
     List<RoleMaster> findAllIsacitve(@Param("isActive") Boolean isActive);
}
