package com.ims.inventory.domen.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecentSaleResponse {
    String reference;
    String customer;
    String status;
    BigDecimal shippingCost;
    BigDecimal taxAmt;
    BigDecimal grandTotal;
    String paymentStatus;
}
