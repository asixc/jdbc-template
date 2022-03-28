package com.example.dao;

import com.example.JdbcApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Map;

@Component
public class SimpleJdbcInsertDAO {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleJdbcInsertDAO.class);
    private final SimpleJdbcInsert simpleJdbcInsert;

    public SimpleJdbcInsertDAO(DataSource dataSource) {
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("products").usingGeneratedKeyColumns("id");
    }

    public void insert(){
        restoreSchema();
        Map<String, Object> parameters = Map.of(
                "name", "product-1",
                "description", "description fake",
                "price", new BigDecimal("20.30")
        );
        Number idProduct = simpleJdbcInsert.executeAndReturnKey(parameters);
        LOG.info("idProduct inserted: {}" , idProduct.longValue());
    }

    private void restoreSchema() {
        JdbcTemplate jdbcTemplate = this.simpleJdbcInsert.getJdbcTemplate();
        jdbcTemplate.execute("DROP TABLE IF EXISTS products;");
        jdbcTemplate.execute("""
                CREATE TABLE products (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(255),
                    description VARCHAR(255),
                    price DECIMAL 
                );
                """);
    }
}
