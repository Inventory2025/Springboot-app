package com.ims.inventory.repository.impl;

import com.ims.inventory.domen.request.Filter;
import com.ims.inventory.domen.request.Page;
import com.ims.inventory.domen.response.ApiResponse;
import com.ims.inventory.repository.PageableJdbcRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PageableJdbcRepositoryImpl implements PageableJdbcRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public <T> ApiResponse<T> findPageableDataWithFilters(String baseQueryStr, List<Filter> filters, Page page,
                                                   RowMapper<T> rowMapper) {

        StringBuilder condition = new StringBuilder();
        StringBuilder orders = new StringBuilder();
        // Map parameters dynamically
        Map<String, Object> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(filters)) {
            for (Filter filter : filters) {
                if (!ObjectUtils.isEmpty(filter.getColumn())) {
                    condition.append(!condition.isEmpty() ? " AND " : "");
                    condition.append(" LOWER(").append(filter.getAlies()).append(".").append(filter.getColumn()).append(") ");
                    condition.append(filter.getOperator()).append(" :").append(filter.getColumn());
                    params.put(filter.getColumn(), "%" + filter.getValue().toLowerCase() + "%");

                    if (!ObjectUtils.isEmpty(filter.getOrder())) {
                        orders.append(!orders.isEmpty() ? ", " : "");
                        orders.append(filter.getAlies()).append(".").append(filter.getColumn())
                                .append(" ").append(filter.getOrder());
                    }
                }
            }
        }

        StringBuilder query = new StringBuilder(baseQueryStr);
        query.append(!orders.isEmpty() ? " where " : " ").append(condition);

        // Count total records
        String countQuery = "SELECT COUNT(*) FROM (" + query + ") AS countTable";
        Long totalElements = jdbcTemplate.queryForObject(countQuery, params, Long.class);

        // Apply sorting and pagination

        if (!ObjectUtils.isEmpty(orders)) {
            query.append(" ORDER BY ").append(orders);
        }
        query.append(" LIMIT :limit OFFSET :offset");
        params.put("limit", page.getSize());
        params.put("offset", page.getPage());

        // Execute paginated query
        List<T> content = jdbcTemplate.query(query.toString(), params, rowMapper);

        return new ApiResponse(true, "Success", content, totalElements,
                page.getPage(), page.getSize());

    }
}
