package com.ims.inventory.service.impl;

import com.ims.inventory.domen.entity.*;
import com.ims.inventory.domen.request.CategoryRequest;
import com.ims.inventory.domen.request.LoadRequest;
import com.ims.inventory.domen.request.ProductRequest;
import com.ims.inventory.domen.request.RemoveRequest;
import com.ims.inventory.domen.response.*;
import com.ims.inventory.exception.ImsBusinessException;
import com.ims.inventory.mappers.RoleRowMapper;
import com.ims.inventory.repository.*;
import com.ims.inventory.repository.impl.ProductRepository;
import com.ims.inventory.utility.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.ims.inventory.constants.ErrorCode.*;
import static com.ims.inventory.constants.ErrorMsg.*;
import static com.ims.inventory.constants.ImsConstants.SUCCESS;

@Slf4j
@Component("ProductMasterServiceImpl")
public class ProductMasterServiceImpl implements ProductMasterservice{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private ProductStockRepository productStockRepository;


    @Override
    public List<ProductResponse> findAllProductByIsActive(Boolean isActive) throws ImsBusinessException {
        log.info("ProductMasterServiceImpl::findAllProductByIsActive:: called for isActive :{}", isActive);
        List<ProductMaster> productList = productRepository.findAll();
        if (!ObjectUtils.isEmpty(productList)) {
            return productList.stream().map(obj -> {
                ProductResponse product = new ProductResponse();
                product.setName(obj.getName());
                return product;
            }).toList();
        } else {
            log.info("ProductMasterServiceImpl::findAllProductByIsActive:: product data not found.");
            throw new ImsBusinessException("ProdOO1", "product not found.");
        }
    }

    private ProductResponse createResponse(ProductMaster productMaster, String method) throws ImsBusinessException {
        if (ObjectUtils.isNotEmpty(productMaster)) {
            ProductResponse resp = new ProductResponse();
            resp.setName(productMaster.getName());
            resp.setStatus(SUCCESS);
            resp.setMessage("Product " + method + "successfully.");
            return resp;
        } else {
            throw new ImsBusinessException(CATEGORY_NOT_FOUND_CODE,
                    "Product not " + method + "successfully.");
        }
    }

    @Override
    public ProductResponse addProduct(ProductRequest productRequest) throws Exception {
        log.info("ProductMasterService::addProduct request :{}", productRequest);
        try {
            ProductMaster productMaster = new ProductMaster();
            productMapper(productMaster, productRequest);
            productMaster.setActive(true);
            ProductMaster product = productRepository.save(productMaster);
            log.info("ProductMasterService::addProduct:Product save successfully.");
            return createResponse(product, "Add");
        } catch (Exception e) {
            throw new ImsBusinessException(CATEGORY_ADD_EXCEPTION_CODE, CATEGORY_ADD_EXCEPTION_MSG);
        }
    }

    @Override
    public ProductResponse editProduct(ProductRequest productRequest) throws Exception {
        log.info("ProductMasterService::Edit product request :{}", productRequest);
        try {
            ProductMaster productMaster = loadProductByName(productRequest.getName());
            productMapper(productMaster, productRequest);
            productMaster.setActive(true);
            productMaster = productRepository.save(productMaster);
            log.info("ProductService::addProduct:Product edit successfully.");
            return createResponse(productMaster, "Edit");
        } catch (Exception e) {
            log.error("ProductMasterService::editProduct::Exception occurred in edit Product for name :{}",
                    productRequest.getName(), e);
            throw new ImsBusinessException(CATEGORY_EDIT_EXCEPTION_CODE, CATEGORY_EDIT_EXCEPTION_MSG);
        }
    }


    @Override
    public ProductDetailResponse loadProduct(ProductRequest productRequest) throws Exception {
        log.info("ProductMasterService::Load product request :{}", productRequest);
        try {
            ProductDetailResponse productResp = new ProductDetailResponse();
            ProductMaster productMaster = loadProductByName(productRequest.getName());
            productDetailMapper(productResp, productMaster);
            if (ObjectUtils.isNotEmpty(productResp)) {
                log.info("ProductService::loadProduct :: Product load successfully.");
                Optional<ProductStock> productStock = productStockRepository.findById(productMaster.getId());
                if (productStock.isPresent()) {
                    Integer pStock = productStock.get().getStock();
                    productResp.setStock((ObjectUtils.isNotEmpty(pStock) && pStock > 0 ? pStock : 0));
                }
                return productResp;
            } else {
                throw new ImsBusinessException(PRODUCT_NOT_FOUND_CODE,
                        "Product not load successfully.");
            }
        } catch (Exception e) {
            log.error("ProductMasterService::loadProduct::Exception occurred in load Product for name :{}",
                    productRequest.getName(), e);
            throw new ImsBusinessException(PRODUCT_LOAD_EXCEPTION_CODE, PRODUCT_LOAD_EXCEPTION_MSG);
        }
    }

    @Override
    public ProductResponse ProductDelete(RemoveRequest removeRequest) throws Exception {
        log.info("ProductMasterService::ProductDelete:: delete product request :{}", removeRequest);
        try {
            ProductMaster productMasterObj = loadProductByName(removeRequest.getId());
            productRepository.delete(productMasterObj);
            ProductResponse resp = new ProductResponse();
            resp.setName(removeRequest.getId());
            resp.setStatus(SUCCESS);
            resp.setMessage("Product delete successfully.");
            log.info("ProductMasterService::addProduct:Product delete successfully.");
            return resp;
        } catch (Exception e) {
            throw new ImsBusinessException(CATEGORY_DELETE_EXCEPTION_CODE, CATEGORY_DELETE_EXCEPTION_MSG);
        }
    }

    private ProductMaster loadProductByName(String name) throws ImsBusinessException {
        log.info("ProductMasterService::loadProductByName:Load Product called.");
        Optional<ProductMaster> productMasterObj = productRepository.findByCode(name);
        if (productMasterObj.isPresent() && ObjectUtils.isNotEmpty(productMasterObj.get())) {
            log.info("ProductMasterService::loadProductByName:Product found.");
            return productMasterObj.get();
        } else {
            throw new ImsBusinessException(CATEGORY_NOT_FOUND_CODE, CATEGORY_NOT_FOUND_MSG);
        }
    }

    private void productMapper(ProductMaster productMaster, ProductRequest productRequest) {
        log.info("ProductMasterService::productMapper:Product mapper called.");
        productMaster.setCode(Util.generateCustomId());
        productMaster.setName(productRequest.getName());
        productMaster.setCategory(categoryRepository.findById(productRequest.getCategoryId()).orElse(null));
        productMaster.setBrand(brandRepository.findById(productRequest.getBrandId()).orElse(null));
        productMaster.setPrice(productRequest.getPrice());
        productMaster.setSaleUnit(unitRepository.findById(productRequest.getUnitId()).orElse(null));
        productMaster.setProductUnit(unitRepository.findById(productRequest.getUnitId()).orElse(null));
        productMaster.setPurchaseUnit(unitRepository.findById(productRequest.getUnitId()).orElse(null));
        productMaster.setQuantity(productRequest.getQuantity());

        productMaster.setDescription(productRequest.getName());
        productMaster.setCost(productRequest.getPrice());
        productMaster.setDiscount(BigDecimal.valueOf(5));
        productMaster.setStockAlert(BigDecimal.valueOf(5));
        productMaster.setTaxPer(BigDecimal.valueOf(18));
        productMaster.setTaxType("Inclusive");
        productMaster.setStatus("true");
    }



    private void productDetailMapper(ProductDetailResponse productMaster, ProductMaster productRequest) {
        log.info("ProductMasterService::productDetailMapper:Product mapper called.");
        productMaster.setCode(productRequest.getCode());
        productMaster.setName(productRequest.getName());
        productMaster.setDescription(productRequest.getDescription());
        productMaster.setCategory(productRequest.getCategory());
        productMaster.setBrand(productRequest.getBrand());
        productMaster.setCost(productRequest.getCost());
        productMaster.setPrice(productRequest.getPrice());
        productMaster.setProductUnit(productRequest.getProductUnit());
        productMaster.setSaleUnit(productRequest.getSaleUnit());
        productMaster.setPurchaseUnit(productRequest.getPurchaseUnit());
        productMaster.setQuantity(productRequest.getQuantity());
        productMaster.setStockAlert(productRequest.getStockAlert());
        productMaster.setDiscount(productRequest.getDiscount());
        productMaster.setTaxPer(productRequest.getTaxPer());
        productMaster.setTaxType(productRequest.getTaxType());
        productMaster.setImageUrl(productRequest.getImageUrl());
        productMaster.setStatus(productRequest.getStatus());
        productMaster.setDescription(productRequest.getDescription());
        productMaster.setStock(0);
    }

    @Override
    public List<AutoCompleteResponse> findAllProductByNameIsActive(String name, Boolean isActive) throws ImsBusinessException {
        List<ProductMaster> productList = null;
        if (StringUtils.isEmpty(name)) {
            productList = productRepository.findTop15ByIsActiveOrderByNameAsc(isActive);
        } else {
            productList = productRepository.findByIsActiveAndNameIgnoreCaseContaining(isActive, name);
        }
        if (!ObjectUtils.isEmpty(productList)) {
            return productList.stream().map(obj -> {
                AutoCompleteResponse resp = new AutoCompleteResponse();
                resp.setId(obj.getId());
                resp.setName(obj.getName());
                resp.setOption(obj.getCode());
                return resp;
            }).toList();
        } else {
            log.info("ProductMasterServiceImpl::findAllProductByNameIsActive:: search product data not found.");
            throw new ImsBusinessException("ItemsOO1", "Items not found.");
        }
    }

    public ProductRequest loadProductMater(LoadRequest loadRequest) throws ImsBusinessException {
        ProductMaster productTran = productRepository.findByIdAndIsActive(loadRequest.getRecordCode(), true);
        if (ObjectUtils.isNotEmpty(productTran)) {
            return mapperDto(productTran);
        } else {
            throw new ImsBusinessException("Sale01", "Sale not found for id :"+loadRequest.getRecordCode());
        }
    }

    private ProductRequest mapperDto(ProductMaster productTran) {
        ProductRequest product = new ProductRequest();
        product.setName(productTran.getName());
        CategoryMaster categoryMaster = categoryRepository.findByIdAndIsActive(productTran.getCategory().getId(), true);
        product.setCategoryId(categoryMaster.getId());

        BradMaster brandMaster = brandRepository.findByIdAndIsActive(productTran.getBrand().getId(), true);
        product.setBrandId(brandMaster.getId());
        product.setPrice(productTran.getPrice());

        UnitMaster unitMaster = unitRepository.findByIdAndIsActive(productTran.getSaleUnit().getId(), true);
        product.setUnitId(unitMaster.getId());
        product.setQuantity(productTran.getQuantity());
        return product;
    }

}
