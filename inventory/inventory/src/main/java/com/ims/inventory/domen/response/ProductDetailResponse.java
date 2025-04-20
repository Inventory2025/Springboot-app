package com.ims.inventory.domen.response;

import com.ims.inventory.domen.entity.BradMaster;
import com.ims.inventory.domen.entity.CategoryMaster;
import com.ims.inventory.domen.entity.UnitMaster;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailResponse {

    private String code;
    private String name;
    private CategoryMaster category;
    private BradMaster brand;
    private BigDecimal cost;
    private BigDecimal price;
    private UnitMaster productUnit;
    private UnitMaster saleUnit;
    private UnitMaster purchaseUnit;
    private Integer quantity;
    private Integer stock;
    private BigDecimal stockAlert;
    private BigDecimal discount;
    private BigDecimal taxPer;
    private String taxType;
    private String imageUrl;
    private String status;
    private String description;

}
