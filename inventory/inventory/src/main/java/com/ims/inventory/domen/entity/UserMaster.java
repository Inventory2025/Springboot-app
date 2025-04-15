package com.ims.inventory.domen.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tbl_user", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class UserMaster extends AuditBaseEntity {

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "password")
    private String password;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "description")
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private RoleMaster roleMaster;

  /*  @Enumerated(EnumType.STRING)
    private UserRole role;*/

}
