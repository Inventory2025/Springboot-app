package com.ims.inventory.domen.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "bm_comp_detail", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class BMCompDetail extends  AbstractBaseEntity {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "comp_id", referencedColumnName = "id")
    private BMComponent bmComponent;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "select_query", columnDefinition = "TEXT")
    private String selectQuery;

    @Column(name = "insert_query")
    private String insertQuery;

    @Column(name = "description")
    private String description;

    @Column(name = "design", columnDefinition = "TEXT")
    private String design;

    @Column(name = "custom_logic_enabled")
    private boolean customLogicEnabled;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bmComponent", cascade = CascadeType.ALL)
    private List<BMCompElements> bmCompElementsList;

}
