package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleItemRepository extends JpaRepository<SaleItem, String> {
}

