package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.MenuMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<MenuMaster, String>  {
}
