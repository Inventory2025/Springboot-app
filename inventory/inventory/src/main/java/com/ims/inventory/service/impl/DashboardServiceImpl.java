package com.ims.inventory.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ims.inventory.constants.ImsConstants;
import com.ims.inventory.domen.response.*;
import com.ims.inventory.service.JdbcTemplateService;
import com.ims.inventory.utility.Util;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl {

    private final JdbcTemplateService jdbcTemplateService;
    private final ObjectMapper objectMapper;

    public String queryCondReplace(Boolean isAdmin_Menu, String query, String condition, String pattern) {
        if (isAdmin_Menu) {
            query = query.replace(pattern, "");
        } else {
            query = query.replace(pattern, condition);
        }
        return query;
    }

    public List<RecentSaleResponse> resentSales(HttpServletRequest request) {

        Boolean isAdminFlag = (Boolean) request.getAttribute(ImsConstants.IS_ADMIN_ROLE);
        String branchId = (String) request.getAttribute(ImsConstants.BRANCH_ID);

        String query = "select trans_code as reference, customer_name, tran_ststus, order_tax, shipping_cost, \n" +
                "total_amount, status \n" +
                "from vm_sale_detail\n" +
                "where status != 'Retun' $$BRANCH$$ order by sale_date desc limit 5";

        query = queryCondReplace(isAdminFlag, query, " and branch_id = '"+branchId+"' ", "$$BRANCH$$");


        List<Map<String, Object>> result = jdbcTemplateService.queryForList(query);
        return mapToRecentSaleResponse(result);

    }

    public String convertListToJson(List<Map<String, Object>> data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            throw new RuntimeException("JSON conversion failed", e);
        }
    }

    public List<RecentSaleResponse> mapToRecentSaleResponse(List<Map<String, Object>> rawList) {
        List<RecentSaleResponse> result = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(rawList)) {
            for (Map<String, Object> row : rawList) {
                RecentSaleResponse dto = new RecentSaleResponse();
                dto.setReference((String) row.get("reference"));
                dto.setCustomer((String) row.get("customer_name"));
                dto.setStatus((String) row.get("tran_ststus"));
                dto.setShippingCost(toBigDecimal(row.get("shipping_cost")));
                dto.setTaxAmt(toBigDecimal(row.get("order_tax")));
                dto.setGrandTotal(toBigDecimal(row.get("total_amount")));
                dto.setPaymentStatus((String) row.get("status"));

                result.add(dto);
            }
        }
        return result;
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value != null) {
            return new BigDecimal(value.toString());
        } else {
            return BigDecimal.ZERO;
        }
    }

    public List<CardsResponse> getCards(HttpServletRequest request) {

        Boolean isAdminFlag = (Boolean) request.getAttribute(ImsConstants.IS_ADMIN_ROLE);
        String branchId = (String) request.getAttribute(ImsConstants.BRANCH_ID);

        String query = "select type, status, sum(total_amount) as total_amount  \n" +
                "from vm_sale_purchase \n" +
                "where $$BRANCH$$ status in ('Paid', 'Retun')\n" +
                "group by type, status";

        query =  queryCondReplace(isAdminFlag, query, " branch_id = '"+branchId+"' and ", "$$BRANCH$$");

        List<Map<String, Object>> result = jdbcTemplateService.queryForList(query);
        List<CardsResponse> cardList = new ArrayList<>();
        CardsResponse sale = new CardsResponse();
        sale.setLabel("SALES");
        sale.setValue("0.00");
        CardsResponse saleR = new CardsResponse();
        saleR.setLabel("SALES RETURN");
        saleR.setValue("0.00");
        CardsResponse purchase = new CardsResponse();
        purchase.setLabel("PURCHASES");
        purchase.setValue("0.00");
        CardsResponse purchaseR = new CardsResponse();
        purchaseR.setLabel("PURCHASES RETURN");
        purchaseR.setValue("0.00");
        if (ObjectUtils.isNotEmpty(result)) {
            for (Map<String, Object> row : result) {
                if ("SALE".equalsIgnoreCase((String) row.get("type"))) {
                    if ("Paid".equalsIgnoreCase((String) row.get("status"))) {
                        sale.setValue(Util.formatToIndianCurrency(row.get("total_amount")));
                    } else if ("Retun".equalsIgnoreCase((String) row.get("status"))) {
                        saleR.setValue(Util.formatToIndianCurrency(row.get("total_amount")));
                    }
                }
                if ("PURCHASE".equalsIgnoreCase((String) row.get("type"))) {
                    if ("Paid".equalsIgnoreCase((String) row.get("status"))) {
                        purchase.setValue(Util.formatToIndianCurrency(row.get("total_amount")));
                    } else if ("Retun".equalsIgnoreCase((String) row.get("status"))) {
                        purchaseR.setValue(Util.formatToIndianCurrency(row.get("total_amount")));
                    }
                }
            }
        }
        cardList.add(sale);
        cardList.add(saleR);
        cardList.add(purchase);
        cardList.add(purchaseR);
        return cardList;
    }

    public List<StockAlertsResponse> resentStockAlert(HttpServletRequest request) {

        Boolean isAdminFlag = (Boolean) request.getAttribute(ImsConstants.IS_ADMIN_ROLE);
        String branchId = (String) request.getAttribute(ImsConstants.BRANCH_ID);

        String query = "select tp.code, tp.\"name\" as product, tb.\"name\" as branch, st.stock, tp.stock_alert \n" +
                "from vm_product_stock st\n" +
                "inner join tbl_product tp on tp.id = st.product_id\n" +
                "inner join tbl_branch tb on tb.id = st.branch_id\n" +
                "where $$BRANCH$$ st.stock < tp.stock_alert limit 5";

        query =  queryCondReplace(isAdminFlag, query, " tb.id = '"+branchId+"' and ", "$$BRANCH$$");

        List<Map<String, Object>> result = jdbcTemplateService.queryForList(query);
        return mapToStockAlertsResponse(result);

    }

    public List<StockAlertsResponse> mapToStockAlertsResponse(List<Map<String, Object>> rawList) {
        List<StockAlertsResponse> result = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(rawList)) {
            for (Map<String, Object> row : rawList) {
                StockAlertsResponse dto = new StockAlertsResponse();
                dto.setCode((String) row.get("code"));
                dto.setProduct((String) row.get("product"));
                dto.setBranch((String) row.get("branch"));
                dto.setQuantity(((Number) row.get("stock")).intValue());
                dto.setAlertQuantity(((Number) row.get("stock_alert")).intValue());
                result.add(dto);
            }
        }
        return result;
    }

    public ChartDataResponse getSellingProducts(HttpServletRequest request) {

        Boolean isAdminFlag = (Boolean) request.getAttribute(ImsConstants.IS_ADMIN_ROLE);
        String branchId = (String) request.getAttribute(ImsConstants.BRANCH_ID);

        String query = "select tp.id as product_id, coalesce(tp.\"name\", '') as product, coalesce(sum(tsi.quantity), 0) as quantity \n" +
                "from tbl_sale ts \n" +
                "inner join tbl_sale_item tsi on tsi.sale_id = ts.id\n" +
                "inner join tbl_product tp on tp.id = tsi.product_id \n" +
                "where $$BRANCH$$ ts.is_active = true and tsi.is_active = true\n" +
                "group by tp.id, tp.\"name\" order by quantity desc limit 5";

        query = queryCondReplace(isAdminFlag, query, " ts.branch_id = '"+branchId+"' and ", "$$BRANCH$$");

        List<Map<String, Object>> result = jdbcTemplateService.queryForList(query);
        return buildChartData(result);
    }

    public ChartDataResponse buildChartData(List<Map<String, Object>> rawList) {
        List<String> labels = new ArrayList<>();
        List<Integer> data = new ArrayList<>();
        List<String> colors = List.of("#6f42c1", "#007bff", "#ffc107", "#dc3545"); // can be dynamic
        ChartDataset dataset = new ChartDataset();
        if (ObjectUtils.isNotEmpty(rawList)) {
            for (Map<String, Object> row : rawList) {
                labels.add((String) row.get("product"));
                data.add(((Number) row.get("quantity")).intValue());
            }
            dataset.setLabel("Top Products");
            dataset.setData(data);
            dataset.setBackgroundColor(colors.subList(0, Math.min(data.size(), colors.size())));
        }
        ChartDataResponse response = new ChartDataResponse();
        response.setLabels(labels);
        response.setDatasets(List.of(dataset));
        return response;
    }


    public BarChartDataResponse getSellAndPurchase(HttpServletRequest request) {

        Boolean isAdminFlag = (Boolean) request.getAttribute(ImsConstants.IS_ADMIN_ROLE);
        String branchId = (String) request.getAttribute(ImsConstants.BRANCH_ID);

        String query = "SELECT   day,  SUM(sales) AS sales,  SUM(purchases) AS purchases\n" +
                "\tFROM (\n" +
                "\t\t  SELECT \n" +
                "\t\t    TO_CHAR(sd.sale_date, 'Dy') AS day,\n" +
                "\t\t    EXTRACT(DOW FROM sd.sale_date) AS dow,\n" +
                "\t\t    SUM(sd.total_amount) AS sales,\n" +
                "\t\t    0 AS purchases\n" +
                "\t\t  FROM vm_sale_detail sd\n" +
                "\t\t  WHERE $$BRANCH1$$  sd.status != 'Retun'\n" +
                "\t\t  GROUP BY day, dow\n" +
                "\t  UNION ALL\n" +
                "\t\tSELECT TO_CHAR(pd.purchase_date, 'Dy') AS day, EXTRACT(DOW FROM pd.purchase_date) AS dow,\n" +
                "\t\t    0 AS sales, SUM(pd.total_amount) AS purchases\n" +
                "\t\t  FROM vm_purchase_detail pd\n" +
                "\t\t  WHERE $$BRANCH2$$ pd.status != 'Retun'\n" +
                "\t\t  GROUP BY day, dow\n" +
                "\t) AS combined\n" +
                "GROUP BY day, dow\n" +
                "ORDER BY dow";

        query = queryCondReplace(isAdminFlag, query, " sd.branch_id = '"+branchId+"' and ", "$$BRANCH1$$");
        query = queryCondReplace(isAdminFlag, query, " pd.branch_id = '"+branchId+"' and ", "$$BRANCH2$$");

        List<Map<String, Object>> result = jdbcTemplateService.queryForList(query);
        return buildBarChartData(result);
    }

    public BarChartDataResponse buildBarChartData(List<Map<String, Object>> rows) {
        List<String> labels = new ArrayList<>();
        List<Integer> sales = new ArrayList<>();
        List<Integer> purchases = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            labels.add((String) row.get("day"));
            sales.add(((Number) row.get("sales")).intValue());
            purchases.add(((Number) row.get("purchases")).intValue());
        }

        BarChartDataset salesDataset = new BarChartDataset();
        salesDataset.setLabel("Sales");
        salesDataset.setData(sales);
        salesDataset.setBackgroundColor("#0d6efd");

        BarChartDataset purchasesDataset = new BarChartDataset();
        purchasesDataset.setLabel("Purchases");
        purchasesDataset.setData(purchases);
        purchasesDataset.setBackgroundColor("#ffc107");

        BarChartDataResponse response = new BarChartDataResponse();
        response.setLabels(labels);
        response.setDatasets(List.of(salesDataset, purchasesDataset));

        return response;
    }

    public DonutChartDataResponse getTopCustomer(HttpServletRequest request) {

        Boolean isAdminFlag = (Boolean) request.getAttribute(ImsConstants.IS_ADMIN_ROLE);
        String branchId = (String) request.getAttribute(ImsConstants.BRANCH_ID);

        String query = "select customer_name, sum(total_amount) as total_amount \n" +
                "from vm_sale_detail \n" +
                "$$BRANCH$$ group by customer_name order by total_amount desc limit 5";

        query = queryCondReplace(isAdminFlag, query, " where branch_id = '"+branchId+"' ", "$$BRANCH$$");

        List<Map<String, Object>> result = jdbcTemplateService.queryForList(query);
        return buildChartDataForCustomer(result);
    }

    public DonutChartDataResponse buildChartDataForCustomer(List<Map<String, Object>> rawList) {
        List<String> labels = new ArrayList<>();
        List<BigDecimal> data = new ArrayList<>();
        List<String> colors = List.of("#6f42c1", "#007bff", "#ffc107", "#dc3545"); // can be dynamic
        DonutChartDataset dataset = new DonutChartDataset();
        if (ObjectUtils.isNotEmpty(rawList)) {
            for (Map<String, Object> row : rawList) {
                labels.add((String) row.get("customer_name"));
                data.add(toBigDecimal(row.get("total_amount")));
            }
            dataset.setLabel("Top Customer");
            dataset.setData(data);
            dataset.setBackgroundColor(colors.subList(0, Math.min(data.size(), colors.size())));
        }
        DonutChartDataResponse response = new DonutChartDataResponse();
        response.setLabels(labels);
        response.setDatasets(List.of(dataset));
        return response;
    }



}
