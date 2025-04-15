package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.BranchMaster;
import com.ims.inventory.domen.entity.CategoryMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryMaster, String> {
    Optional<CategoryMaster> findByName(String name);
}