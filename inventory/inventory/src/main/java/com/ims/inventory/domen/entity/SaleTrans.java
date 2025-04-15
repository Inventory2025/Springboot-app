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
public class SaleTrans extends AuditBaseEntity{

    @Column(name = "sale_date")
    private LocalDateTime saleDate;

    @Column(name = "trans_code", nullable = false, unique = true)
    private String transCode;

    @Column(name = "totalAmount")
    private BigDecimal totalAmount;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerMaster customerMaster;

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

    @OneToMany(mappedBy = "saleTrans", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItem> items;

    @Column(name = "remarks")
    private String remarks;
}
