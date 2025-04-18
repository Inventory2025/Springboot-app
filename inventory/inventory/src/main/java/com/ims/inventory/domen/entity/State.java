package com.ims.inventory.domen.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tbl_state_master", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class State extends AuditBaseEntity {

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    private String name;

    // Many states belong to one country
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL)
    private List<City> cities;
}