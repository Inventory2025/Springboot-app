package com.ims.inventory.domen.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tbl_sale", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class SaleMaster extends AuditBaseEntity{

    @Column(name = "sale_date")
    private LocalDateTime saleDate;

    @Column(name = "totalAmount")
    private BigDecimal totalAmount;

    @Column(name = "customerName")
    private String customerName;

    @Column(name = "order_tax_per")
    private BigDecimal orderTaxPer;

    @Column(name = "discount_per")
    private BigDecimal discountPer;

    @Column(name = "order_tax")
    private BigDecimal orderTax;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "shipping_cost")
    private BigDecimal shippingCost;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private BranchMaster branchMaster;

    @ManyToOne
    @JoinColumn(name = "sales_man")
    private UserMaster salesMan;

    @OneToMany(mappedBy = "saleMaster", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItem> items;

    @Column(name = "remarks")
    private String remarks;
}
