package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStockRepository extends JpaRepository<ProductStock, String> {

}
