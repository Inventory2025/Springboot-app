package com.ims.inventory.domen.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tbl_country_master", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class Country extends AuditBaseEntity {

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    private String name;

    // One country has many states
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    private List<State> states;
}