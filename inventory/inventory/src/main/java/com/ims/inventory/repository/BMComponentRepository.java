package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.BMComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BMComponentRepository extends JpaRepository<BMComponent, Long> {

    BMComponent findByCode(String code);

    @Query("select ce from BMComponent ce where ce.parent IS NULL")
    List<BMComponent> findByParentIsNull();

    @Query("select ce from BMComponent ce where ce.parent IS NULL order by orderBy")
    List<BMComponent> findByParentIsNullOrderByOrder();

}
