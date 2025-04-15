package com.ims.inventory.domen.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tbl_product", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class ProductMaster extends AuditBaseEntity {

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryMaster category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private BradMaster brand;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "price")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "product_unit")
    private UnitMaster productUnit;

    @ManyToOne
    @JoinColumn(name = "sale_unit")
    private UnitMaster saleUnit;

    @ManyToOne
    @JoinColumn(name = "purchase_unit")
    private UnitMaster purchaseUnit;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "stock_alert")
    private BigDecimal stockAlert;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "tax_per")
    private BigDecimal taxPer;

    @Column(name = "tax_type")
    private String taxType;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "status")
    private String status;

    @Column(name = "description")
    private String description;


}
