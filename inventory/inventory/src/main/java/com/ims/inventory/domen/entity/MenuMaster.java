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
@Table(name = "tbl_menu", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class MenuMaster extends AuditBaseEntity {

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "label")
    private String label;

    @Column(name = "description")
    private String description;

    @Column(name = "parent")
    private String parent;

    @Column(name = "icon")
    private String icon;

    @Column(name = "menu_order")
    private Integer menuOrder;

}
