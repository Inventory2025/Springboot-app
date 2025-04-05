package com.ims.inventory.domen.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tbl_role_menu_map", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class RoleMenuMap extends AuditBaseEntity {

    @Column(name = "role_id", nullable = false)
    private String roleId;

    @Column(name = "menu_id", nullable = false)
    private String menuId;

}
