package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.UserRoleMap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleMapRepository extends JpaRepository<UserRoleMap, String>  {
}
