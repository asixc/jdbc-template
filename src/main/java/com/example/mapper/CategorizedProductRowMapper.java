package com.example.mapper;

import com.example.domain.CategorizedProduct;
import com.example.domain.Category;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CategorizedProductRowMapper implements RowMapper<CategorizedProduct> {

    private final JdbcTemplate jdbcTemplate;

    public CategorizedProductRowMapper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public CategorizedProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
        final String sql = "SELECT * FROM categories where product_id = " + rs.getLong("id");
        List<Category> categories = jdbcTemplate.query(sql, MAP_CATEGORY_FROM_PRODUCT);
        return new CategorizedProduct(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getBigDecimal("price"),
                categories
        );

    }

    private final  RowMapper<Category> MAP_CATEGORY_FROM_PRODUCT = new RowMapper<Category>() {
        @Override
        public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Category(
                    rs.getLong("id"),
                    rs.getString("category_name"),
                    rs.getString("product_id"));
        }
    };
}
