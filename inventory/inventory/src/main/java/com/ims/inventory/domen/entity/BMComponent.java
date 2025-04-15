package com.ims.inventory.domen.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "bm_component", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class BMComponent extends  AbstractBaseEntity {

    @Column(name = "code")
    private String code;

    @Column(name = "c_name")
    private String name;

    @Column(name = "c_label")
    private String label;

    @Column(name = "comp_desc")
    private String description;

    @Column(name = "c_link")
    private String link;

    @Column(name = "sbc_link")
    private String sbcLink;

    @Column(name = "is_component")
    private Boolean isComponent;

    @Column(name = "is_parent")
    private Boolean isParent;

    @Column(name = "order_by")
    private Long orderBy;

    @Column(name = "icon")
    private String icon;

    @Column(name = "image")
    private Byte image;

    @ManyToOne
    @JoinColumn(name = "parent")
    private BMComponent parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("orderBy")
    private List<BMComponent> children;

}
