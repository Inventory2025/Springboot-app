package com.ims.inventory.service;

import com.ims.inventory.domen.response.FilterResponse;
import com.ims.inventory.domen.response.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class JdbcTemplateService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParamJdbcTemplate;

    public FilterResponse getQueryResult(Integer pageNo, Integer pageSize, String query) {
        FilterResponse resp = new FilterResponse();

        List<Map<String, Object>> list = jdbcTemplate.queryForList(query);
        int totalPages = (list.size() / (pageSize > 0 ? pageSize : 1));
        Page page = new Page(
                pageSize,
                pageNo,
                list.size(),
                totalPages > 0 ? totalPages : 1
        );
        resp.setResultList(list);
        resp.setPage(page);
        System.out.println(list);
        return resp;
    }

    public List<Map<String, Object>> executeSelectWithWhereQuery(String tableName, String columns, String recordId) throws Exception {
        try {
            if (ObjectUtils.isNotEmpty(columns)) {
                StringBuffer sb = new StringBuffer();
                sb.append("select ").append(columns).append(" from ").append(tableName)
                        .append(" where ").append(" id = '").append(recordId).append("'");
                return queryForList(sb.toString());
            } else {
                throw new Exception("Columns is null or empty.");
            }
        } catch (Exception ex) {
            log.error("Exception :: executeSelectWithWhereQuery --> {}", ex.getMessage());
            throw ex;
        }
    }

    public int executeUpdateWithWhereQuery(String tableName
            , String columns, String recordId) throws Exception {
        try {
            if (!ObjectUtils.isNotEmpty(columns)) {
                StringBuffer sb = new StringBuffer();
                sb.append("update ").append(tableName).append(" set ").append(columns)
                        .append(" where ").append(" id = ").append(recordId);
                return execute (sb.toString());
            } else {
                throw new Exception("Columns is null or empty.");
            }
        } catch (Exception ex) {
            log.error("Exception :: getQueryResult --> {}", ex.getMessage());
            throw ex;
        }
    }

    private List<Map<String, Object>> queryForList(String query) {
        log.info("queryForList ---->"+query);
        return jdbcTemplate.queryForList(query);
    }

    public int execute(String insertQuery) throws DataAccessException {
        return jdbcTemplate.update(insertQuery);
    }

    public int execute(String insertQuery, Map<String, Object> params) throws Exception {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(params);
        return namedParamJdbcTemplate.update(insertQuery, parameterSource);
    }

}
