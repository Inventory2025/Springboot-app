package com.ims.inventory.mappers;

import com.ims.inventory.domen.response.RoleResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleRowMapper implements RowMapper<RoleResponse> {

    @Override
    public RoleResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new RoleResponse(
                rs.getString("name")
        );
    }

}
