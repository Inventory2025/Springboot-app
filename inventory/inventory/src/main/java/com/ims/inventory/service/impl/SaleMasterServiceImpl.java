package com.ims.inventory.service.impl;

import com.ims.inventory.domen.entity.ProductMaster;
import com.ims.inventory.domen.entity.SaleItem;
import com.ims.inventory.domen.entity.SaleTrans;
import com.ims.inventory.domen.request.SaleRequest;
import com.ims.inventory.repository.SaleRepository;
import com.ims.inventory.repository.impl.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component("SaleMasterServiceImpl")
public class SaleMasterServiceImpl {


    private final SaleRepository saleRepo;
    private final ProductRepository productRepo;


    public SaleMasterServiceImpl(SaleRepository saleRepo, ProductRepository productRepo) {
        this.saleRepo = saleRepo;
        this.productRepo = productRepo;
    }

    public SaleTrans createSale(SaleRequest request) {
        List<SaleItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (SaleRequest.SaleItemDTO item : request.getItems()) {
            ProductMaster product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            BigDecimal lineTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

            // Update product quantity (optional)
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepo.save(product);

            SaleItem ite=SaleItem.builder().product(product).quantity(item.getQuantity())
                    .unitPrice(item.getUnitPrice()).subTotal(lineTotal).build();
            items.add(ite);

            total = total.add(lineTotal);
        }

        SaleTrans sale = SaleTrans.builder()
                .customerMaster(null)
                .remarks(request.getRemarks())
                .totalAmount(total)
                .items(items)
                .build();

        items.forEach(i -> i.setSaleTrans(sale)); // set back-reference

        return saleRepo.save(sale);
    }

    public List<SaleTrans> getAllSales() {
        return saleRepo.findAll();
    }
}
