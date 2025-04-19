package com.ims.inventory.service.impl;

import com.ims.inventory.constants.ImsConstants;
import com.ims.inventory.domen.entity.PurchaseItem;
import com.ims.inventory.domen.entity.PurchaseTrans;
import com.ims.inventory.domen.entity.SaleTrans;
import com.ims.inventory.domen.request.LoadRequest;
import com.ims.inventory.domen.request.PurchaseRequest;
import com.ims.inventory.domen.request.SaleRequest;
import com.ims.inventory.domen.response.ApiResponse;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.helpers.PurchaseInvoicePdfService;
import com.ims.inventory.repository.BranchRepository;
import com.ims.inventory.repository.PurchaseRespository;
import com.ims.inventory.repository.SupplierRepository;
import com.ims.inventory.repository.UserMasterRepository;
import com.ims.inventory.repository.impl.ProductRepository;
import com.ims.inventory.utility.Util;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component("PurchaseServiceImpl")
@RequiredArgsConstructor
public class PurchaseServiceImpl {

    private final PurchaseRespository purchaseRepo;
    private final SupplierRepository supplierRepo;
    private final ProductRepository productRepo;
    private final UserMasterRepository userMasterRepo;
    private final BranchRepository branchRepo;
    private final PurchaseInvoicePdfService purchaseInvoicePdfService;

    public ApiResponse<String> save(PurchaseRequest dto, HttpServletRequest request) throws ImsBusinessException {
        PurchaseTrans purchase = new PurchaseTrans();
        if (ObjectUtils.isNotEmpty(dto.getTranCode())) {
            purchase = purchaseRepo.findByTransCodeAndIsActive(dto.getTranCode(), true).orElse(null);
            if (ObjectUtils.isEmpty(purchase)) {
                throw new ImsBusinessException("PURCH002", "Transaction not found.");
            }
        }
        purchase.setTransCode(ObjectUtils.isEmpty(purchase.getTransCode()) ? Util.generateCustomId() : purchase.getTransCode());
        purchase.setSupplierMaster(supplierRepo.findById(dto.getSupplier()).orElseThrow());
        purchase.setPurchaseDate(LocalDate.parse(dto.getDate()).atStartOfDay());
        purchase.setOrderTax(dto.getOrderTax());
        purchase.setOrderTaxPer(dto.getOrderTaxPer());
        purchase.setShippingCost(dto.getShippingCost());
        purchase.setTotalAmount(dto.getGrandTotal());
        purchase.setRemarks(dto.getNote());
        purchase.setStatus(dto.getStatus());
        purchase.setActive(true);

        // Add new items
        List<PurchaseItem> updatedItems = new ArrayList<>();
        for (PurchaseRequest.PurchaseItemDto obj : dto.getItems()) {
            PurchaseItem item = new PurchaseItem();
            item.setProduct(productRepo.findByCode(obj.getProductCode()).orElseThrow());
            item.setQuantity(obj.getQuantity());
            item.setUnitPrice(obj.getUnitCost());
            item.setTax(obj.getTaxAmt());
            item.setSubTotal(obj.getSubTotal());
            item.setActive(true);
            item.setPurchaseTrans(purchase);
            updatedItems.add(item);
        }
        if (ObjectUtils.isEmpty(purchase.getItems()) ) {
            purchase.setItems(updatedItems);
        } else {
            // Remove old items
            purchase.getItems().clear();
            purchase.getItems().addAll(updatedItems);
        }
        purchase.setSalesMan(userMasterRepo.findById((String) request.getAttribute(ImsConstants.USER_ID)).orElseThrow());
        purchase.setBranchMaster(branchRepo.findById((String) request.getAttribute(ImsConstants.BRANCH_ID)).orElseThrow());
        PurchaseTrans purchaseTrans = purchaseRepo.save(purchase);
        return ApiResponse.success("Purchase successfully for "+purchaseTrans.getTransCode(), purchaseTrans.getTransCode());
    }

    public PurchaseRequest loadPurchase(LoadRequest loadRequest) throws ImsBusinessException {
        PurchaseTrans purchaseTrans = purchaseRepo.findByIdAndIsActive(loadRequest.getRecordCode(), true);
        if (ObjectUtils.isNotEmpty(purchaseTrans)) {
            return mapperDto(purchaseTrans);
        } else {
            throw new ImsBusinessException("PURCH002", "Purchase not found for id :"+loadRequest.getRecordCode());
        }
    }

    private PurchaseRequest mapperDto(PurchaseTrans purchaseTrans) {
        PurchaseRequest purchase = new PurchaseRequest();
        purchase.setSupplier(purchaseTrans.getSupplierMaster().getSupplierName());
        String formattedDate = purchaseTrans.getPurchaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        purchase.setDate(formattedDate);
        purchase.setOrderTax(purchaseTrans.getOrderTax());
        purchase.setOrderTaxPer(purchaseTrans.getOrderTaxPer());
        purchase.setShippingCost(purchaseTrans.getShippingCost());
        purchase.setGrandTotal(purchaseTrans.getTotalAmount());
        purchase.setNote(purchaseTrans.getRemarks());
        purchase.setStatus(purchaseTrans.getStatus());
        List<PurchaseRequest.PurchaseItemDto> items = purchaseTrans.getItems().stream().map(i -> {
            PurchaseRequest.PurchaseItemDto item = new PurchaseRequest.PurchaseItemDto();
            item.setProductCode(i.getProduct().getCode());
            item.setProductName(i.getProduct().getName());
            item.setQuantity(i.getQuantity());
            item.setUnitCost(i.getUnitPrice());
            item.setTaxAmt(i.getTax());
            item.setTax(i.getProduct().getTaxPer());
            item.setSubTotal(i.getSubTotal());
            return item;
        }).collect(Collectors.toList());

        purchase.setItems(items);
        purchase.setTranCode(purchaseTrans.getTransCode());
        return purchase;
    }

    public void createPdf(LoadRequest loadRequest, HttpServletResponse response) throws ImsBusinessException, IOException {
        PurchaseRequest purchaseRequest = loadPurchase(loadRequest);
        if (ObjectUtils.isNotEmpty(purchaseRequest)) {
            purchaseInvoicePdfService.exportInvoice(purchaseRequest, response);
        } else {
            throw new ImsBusinessException("PURCH003", "Purchase not found for id :"+loadRequest.getRecordCode());
        }
    }


}
