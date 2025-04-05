package com.ims.inventory.repository;

import com.ims.inventory.domen.request.Filter;
import com.ims.inventory.domen.request.Page;
import com.ims.inventory.domen.response.ApiResponse;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public interface PageableJdbcRepository {

    <T> ApiResponse<T> findPageableDataWithFilters(String baseQueryStr, List<Filter> filters, Page page,
                                                   RowMapper<T> rowMapper);
}
