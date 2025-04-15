package com.ims.inventory.repository.impl;

import com.ims.inventory.domen.entity.CategoryMaster;
import com.ims.inventory.domen.entity.ProductMaster;
import com.ims.inventory.repository.PageableJdbcRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductMaster, String>, PageableJdbcRepository {
    Optional<ProductMaster> findByName(String name);

}
