package com.ims.inventory.domen.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SaleRequest {

    @NotNull(message = "Customer ID is required")
    private String customer;
    @NotNull(message = "Date is required")
    private String date;

    private BigDecimal discount;
    private BigDecimal discountPer;
    private BigDecimal orderTax;
    private BigDecimal orderTaxPer;
    private BigDecimal shippingCost;

    @Min(value = 0, message = "Grand total must be 0 or greater")
    private BigDecimal grandTotal;
    private String note;

    @NotBlank(message = "Status is required")
    private String status;

    @NotEmpty(message = "At least one item is required")
    private List<SaleItemDto> items;

    @Data
    public static class SaleItemDto {
        @NotBlank(message = "Product code is required")
        private String productCode;

        private String productName;

        @NotNull(message = "Unit cost is required")
        @PositiveOrZero(message = "Unit cost must be 0 or greater")
        private BigDecimal unitCost;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;

        private BigDecimal tax;
        private BigDecimal taxAmt;
        private BigDecimal discount;
        private BigDecimal discountAmt;

        @NotNull(message = "Subtotal is required")
        @PositiveOrZero(message = "Subtotal must be 0 or greater")
        private BigDecimal subTotal;
    }
}
