package com.ims.inventory.domen.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SaleRequest {
    private String customerName;
    private String remarks;
    private List<SaleItemDTO> items;

    @Data
    public static class SaleItemDTO {
        private String productId;
        private Integer quantity;
        private BigDecimal unitPrice;
    }
}
