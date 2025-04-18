package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StateRepository extends JpaRepository<State, String> {

    Optional<State> findByCode(String code);
}
