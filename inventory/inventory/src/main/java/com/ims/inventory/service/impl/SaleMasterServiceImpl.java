package com.ims.inventory.service.impl;

import com.ims.inventory.constants.ImsConstants;
import com.ims.inventory.domen.entity.SaleItem;
import com.ims.inventory.domen.entity.SaleTrans;
import com.ims.inventory.domen.entity.UserMaster;
import com.ims.inventory.domen.request.SaleRequest;
import com.ims.inventory.domen.response.ApiResponse;
import com.ims.inventory.repository.BranchRepository;
import com.ims.inventory.repository.CustomerRepository;
import com.ims.inventory.repository.SaleRepository;
import com.ims.inventory.repository.UserMasterRepository;
import com.ims.inventory.repository.impl.ProductRepository;
import com.ims.inventory.utility.Util;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component("SaleMasterServiceImpl")
@RequiredArgsConstructor
public class SaleMasterServiceImpl {


    private final SaleRepository saleRepo;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepo;
    private final UserMasterRepository userMasterRepo;
    private final BranchRepository branchRepo;

    public ApiResponse<String> save(SaleRequest dto, HttpServletRequest request) {
        SaleTrans sale = new SaleTrans();
        sale.setTransCode(Util.generateCustomId());
        sale.setCustomerMaster(customerRepository.findById(dto.getCustomer()).orElseThrow());
        sale.setSaleDate(LocalDate.parse(dto.getDate()).atStartOfDay());
        sale.setDiscount(dto.getDiscount());
        sale.setDiscountPer(dto.getDiscountPer());
        sale.setOrderTax(dto.getOrderTax());
        sale.setOrderTaxPer(dto.getOrderTaxPer());
        sale.setShippingCost(dto.getShippingCost());
        sale.setTotalAmount(dto.getGrandTotal());
        sale.setRemarks(dto.getNote());
        sale.setStatus(dto.getStatus());
        sale.setActive(true);
        List<SaleItem> items = dto.getItems().stream().map(i -> {
            SaleItem item = new SaleItem();
            item.setProduct(productRepo.findByCode(i.getProductCode()).orElseThrow());
            item.setQuantity(i.getQuantity());
            item.setUnitPrice(i.getUnitCost());
            item.setTax(i.getTaxAmt());
            item.setDiscount(i.getDiscountAmt());
            item.setSubTotal(i.getSubTotal());
            item.setActive(true);
            item.setSaleTrans(sale);
            return item;
        }).collect(Collectors.toList());

        sale.setItems(items);

        sale.setSalesMan(userMasterRepo.findById((String) request.getAttribute(ImsConstants.USER_ID)).orElseThrow());
        sale.setBranchMaster(branchRepo.findById((String) request.getAttribute(ImsConstants.BRANCH_ID)).orElseThrow());

        SaleTrans saleT = saleRepo.save(sale);

        return ApiResponse.success("Sale successfully for "+saleT.getTransCode(), saleT.getTransCode());
    }

    public void softDelete(String id) {
        SaleTrans sale = saleRepo.findById(id).orElseThrow();
        sale.setActive(false);
        saleRepo.save(sale);
    }

    public List<SaleTrans> getAll() {
        return saleRepo.findByIsActive(true);
    }




}
