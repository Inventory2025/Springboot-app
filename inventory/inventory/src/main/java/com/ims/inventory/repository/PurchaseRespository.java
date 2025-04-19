package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.PurchaseTrans;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchaseRespository extends JpaRepository<PurchaseTrans, String> {

    List<PurchaseTrans> findByIsActive(boolean isActive);

    PurchaseTrans findByIdAndIsActive(String id, boolean isActive);

    Optional<PurchaseTrans> findByTransCodeAndIsActive(String transCode, boolean isActive);

}