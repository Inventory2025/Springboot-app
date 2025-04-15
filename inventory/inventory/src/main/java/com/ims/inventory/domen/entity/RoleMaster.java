package com.ims.inventory.domen.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tbl_role", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class RoleMaster extends AuditBaseEntity {

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "expiry_time")
    private Long expiryTime;

    @Column(name = "description")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "roleMaster", cascade = CascadeType.ALL)
    private List<UserMaster> userMasterList;

}
