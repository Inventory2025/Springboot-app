package com.ims.inventory.service.impl;

import com.ims.inventory.constants.ImsConstants;
import com.ims.inventory.domen.entity.SaleItem;
import com.ims.inventory.domen.entity.SaleTrans;
import com.ims.inventory.domen.entity.UserMaster;
import com.ims.inventory.domen.request.LoadRequest;
import com.ims.inventory.domen.request.SaleRequest;
import com.ims.inventory.domen.response.ApiResponse;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.helpers.InvoicePdf2Service;
import com.ims.inventory.helpers.InvoicePdfService;
import com.ims.inventory.repository.BranchRepository;
import com.ims.inventory.repository.CustomerRepository;
import com.ims.inventory.repository.SaleRepository;
import com.ims.inventory.repository.UserMasterRepository;
import com.ims.inventory.repository.impl.ProductRepository;
import com.ims.inventory.utility.Util;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private final InvoicePdfService invoicePdfService;
    private final InvoicePdf2Service invoicePdf2Service;

    public ApiResponse<String> save(SaleRequest dto, HttpServletRequest request) throws ImsBusinessException {
        SaleTrans sale = new SaleTrans();
        if (ObjectUtils.isNotEmpty(dto.getTranCode())) {
            sale = saleRepo.findByTransCodeAndIsActive(dto.getTranCode(), true).orElse(null);
            if (ObjectUtils.isEmpty(sale)) {
                throw new ImsBusinessException("SALE002", "Transaction not found.");
            }
        }
        sale.setTransCode(ObjectUtils.isEmpty(sale.getTransCode()) ? Util.generateCustomId() : sale.getTransCode());
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

        // Add new items
        List<SaleItem> updatedItems = new ArrayList<>();
        for (SaleRequest.SaleItemDto obj : dto.getItems()) {
            SaleItem item = new SaleItem();
            item.setProduct(productRepo.findByCode(obj.getProductCode()).orElseThrow());
            item.setQuantity(obj.getQuantity());
            item.setUnitPrice(obj.getUnitCost());
            item.setTax(obj.getTaxAmt());
            item.setDiscount(obj.getDiscountAmt());
            item.setSubTotal(obj.getSubTotal());
            item.setActive(true);
            item.setSaleTrans(sale);
            updatedItems.add(item);
        }
        if (ObjectUtils.isEmpty(sale.getItems()) ) {
            sale.setItems(updatedItems);
        } else {
            // Remove old items
            sale.getItems().clear();
            sale.getItems().addAll(updatedItems);
        }
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

    public SaleRequest loadSale(LoadRequest loadRequest) throws ImsBusinessException {
        SaleTrans saleTran = saleRepo.findByIdAndIsActive(loadRequest.getRecordCode(), true);
        if (ObjectUtils.isNotEmpty(saleTran)) {
            return mapperDto(saleTran);
        } else {
            throw new ImsBusinessException("Sale01", "Sale not found for id :"+loadRequest.getRecordCode());
        }
    }

    private SaleRequest mapperDto(SaleTrans saleTran) {
        SaleRequest sale = new SaleRequest();
        sale.setCustomer(saleTran.getCustomerMaster().getCustomerName());
        String formattedDate = saleTran.getSaleDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        sale.setDate(formattedDate);
        sale.setDiscount(saleTran.getDiscount());
        sale.setDiscountPer(saleTran.getDiscountPer());
        sale.setOrderTax(saleTran.getOrderTax());
        sale.setOrderTaxPer(saleTran.getOrderTaxPer());
        sale.setShippingCost(saleTran.getShippingCost());
        sale.setGrandTotal(saleTran.getTotalAmount());
        sale.setNote(saleTran.getRemarks());
        sale.setStatus(saleTran.getStatus());
        List<SaleRequest.SaleItemDto> items = saleTran.getItems().stream().map(i -> {
            SaleRequest.SaleItemDto item = new SaleRequest.SaleItemDto();
            item.setProductCode(i.getProduct().getCode());
            item.setProductName(i.getProduct().getName());
            item.setQuantity(i.getQuantity());
            item.setUnitCost(i.getUnitPrice());
            item.setTaxAmt(i.getTax());
            item.setTax(i.getProduct().getTaxPer());
            item.setDiscountAmt(i.getDiscount());
            item.setDiscount(i.getProduct().getDiscount());
            item.setSubTotal(i.getSubTotal());
            return item;
        }).collect(Collectors.toList());

        sale.setItems(items);
        sale.setTranCode(saleTran.getTransCode());
        return sale;
    }

    public void createPdf(LoadRequest loadRequest, HttpServletResponse response) throws ImsBusinessException, IOException {
        SaleRequest sale = loadSale(loadRequest);
        if (ObjectUtils.isNotEmpty(sale)) {
            invoicePdf2Service.exportInvoice(sale, response);
        } else {
            throw new ImsBusinessException("Sale01", "Sale not found for id :"+loadRequest.getRecordCode());
        }
    }

}
