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
@Table(name = "tbl_unit", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class UnitMaster extends AuditBaseEntity{

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "shot_name")
    private String shotName;

    @Column(name = "base_unit")
    private String baseUnit;

    @Column(name = "operator")
    private String operator;

    @Column(name = "operation_value")
    private String operationValue;

    @Column(name = "description")
    private String description;
}
