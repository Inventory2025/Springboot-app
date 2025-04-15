package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.SaleTrans;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<SaleTrans, String> {
}
