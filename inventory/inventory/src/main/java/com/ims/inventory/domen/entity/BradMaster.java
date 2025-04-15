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
@Table(name = "tbl_brand", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class BradMaster extends AuditBaseEntity {

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "image_link")
    private String imageLink;

    @Column(name = "description")
    private String description;

}
