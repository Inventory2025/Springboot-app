package com.ims.inventory.domen.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tbl_role_menu_map", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class RoleMenuMap extends AuditBaseEntity {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private RoleMaster roleMaster;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bMComponent_id", referencedColumnName = "id")
    private BMComponent bMComponent;

}
