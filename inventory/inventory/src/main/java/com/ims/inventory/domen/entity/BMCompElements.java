package com.ims.inventory.domen.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "bm_comp_element", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class BMCompElements extends  AbstractBaseEntity {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "comp_id", referencedColumnName = "id")
    private BMCompDetail bmComponent;

    @Column(name = "code")
    private String code;

    @Column(name = "col_name")
    private String colName;

    @Column(name = "col_datatype")
    private String colDataType;

    @Column(name = "col_label")
    private String colLabel;

    @Column(name = "description")
    private String description;

    @Column(name = "is_unique")
    private Boolean isUnique;

    @Column(name = "is_dropdown")
    private Boolean isDropDown;

    @Column(name = "dropdown_query")
    private String dropDownQuery;

    @Column(name = "str_validation")
    private String srtValidation;

    @Column(name = "is_form")
    private Boolean isForm;

    @Column(name = "id_editable")
    private Boolean isEditable;

    @Column(name = "positions")
    private String positions;

    @Column(name = "design")
    private String design;

    @Column(name = "col_type")
    private String colType;

}
