package com.ims.inventory.domen.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tbl_city_master", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class City extends AuditBaseEntity {

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    private String name;

    // Many cities belong to one state
    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;
}
