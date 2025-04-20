package com.ims.inventory.domen.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "vm_product_stock")
public class ProductStock {
    @Id
    @Column(name = "product_id")
    private String productId;

    @Column(name = "product")
    private String product;

    @Column(name = "branch_id")
    private String branchId;

    @Column(name = "purchase_count")
    private Integer purchaseCount;

    @Column(name = "sale_count")
    private Integer saleCount;

    @Column(name = "stock")
    private Integer stock;

}
