package com.example.dao;

import com.example.domain.Product;
import com.example.mapper.ProductRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueriesWithParameters {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public QueriesWithParameters(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> findByFullNameMap(String nameProduct){

        String sql = "SELECT * FROM products WHERE name = :name";

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("name", nameProduct);

        return jdbcTemplate.query(sql, namedParameters, new ProductRowMapper());

    }

    public List<Product> findByFullNameBean(Product product){

        String sql = "SELECT * FROM products WHERE name = :name ";

        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(product);

        return jdbcTemplate.query(sql, namedParameters, new ProductRowMapper());

    }
}
