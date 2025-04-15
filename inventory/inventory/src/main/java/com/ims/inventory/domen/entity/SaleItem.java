package com.ims.inventory.domen.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Builder
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tbl_sale_item", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class SaleItem extends AuditBaseEntity{

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private SaleTrans saleTrans;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductMaster product;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unitPrice")
    private BigDecimal unitPrice;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "tax")
    private BigDecimal tax;

    @Column(name = "sub_total")
    private BigDecimal subTotal;
}
