package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.BMCompDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BMCompDetailRepository extends JpaRepository<BMCompDetail, Long> {

    @Query("select cd from BMCompDetail cd inner join cd.bmComponent bc where bc.code = :code ")
    public BMCompDetail findByCompCode(String code);

    @Query("select cd.selectQuery from BMCompDetail cd inner join cd.bmComponent bc where bc.code = :code ")
    public String findSelectQueryByCompCode(String code);

    @Query("select cd.insertQuery from BMCompDetail cd inner join cd.bmComponent bc where bc.code = :code ")
    public String findInsertQueryByCompCode(String code);

}
