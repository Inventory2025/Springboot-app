package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.SaleTrans;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<SaleTrans, String> {

    List<SaleTrans> findByIsActive(boolean isActive);


}
