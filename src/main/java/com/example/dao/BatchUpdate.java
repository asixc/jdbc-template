package com.example.dao;

import com.example.domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BatchUpdate {

    private final JdbcTemplate jdbcTemplate;

    public BatchUpdate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean doUpdote(final List<Product> products){

        String sql = "INSERT INTO products (name, description, price) VALUES (?, ?, ?)";
        List<Object[]> productsObjects = new ArrayList<Object[]>();
        products.forEach(
                p -> productsObjects.add(new String[]{p.name(), p.description(), p.price().toString()})
        );
        this.jdbcTemplate.batchUpdate(sql, productsObjects);
        return true;
    }
}





