package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, String> {
}

