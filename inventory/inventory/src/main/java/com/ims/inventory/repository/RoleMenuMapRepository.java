package com.ims.inventory.repository;

import com.ims.inventory.domen.entity.BMComponent;
import com.ims.inventory.domen.entity.RoleMenuMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMenuMapRepository extends JpaRepository<RoleMenuMap, String>  {


    @Query("select bm from RoleMenuMap rm inner join rm.bMComponent bm " +
            " inner join rm.roleMaster r where r.id = ?1 " +
            " and rm.isActive = ?2 and bm.isActive = ?2 and r.isActive = ?2 " +
            " and bm.parent IS NULL order by bm.orderBy")
    List<BMComponent> findByParentIsNullOrderByOrder(String roleId, Boolean isActive);

}
