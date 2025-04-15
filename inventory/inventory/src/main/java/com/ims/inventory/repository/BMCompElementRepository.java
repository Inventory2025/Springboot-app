package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.BMCompElements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BMCompElementRepository extends JpaRepository<BMCompElements, Long> {

    @Query("select ce from BMCompElements ce inner join ce.bmComponent cd inner join cd.bmComponent bc "
            + " where ce.code = :code and bc.code = :compCode ")
    BMCompElements findByCompCodeAndCode(String code, String compCode);

    @Query("select ce from BMCompElements ce inner join ce.bmComponent cd "
            + " where ce.isForm = :isForm and cd.id = :bmComId ")
    List<BMCompElements> findByBmComIdAndIsForm(String bmComId, Boolean isForm);
}
