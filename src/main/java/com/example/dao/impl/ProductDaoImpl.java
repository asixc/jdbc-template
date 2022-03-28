package com.example.dao.impl;

import com.example.dao.BatchUpdate;
import com.example.dao.ProductDao;
import com.example.dao.QueriesWithParameters;
import com.example.dao.SimpleJdbcInsertDAO;
import com.example.domain.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductDaoImpl implements ProductDao {

    private final BatchUpdate batchUpdate;
    private final QueriesWithParameters queryWithParameters;
    private final SimpleJdbcInsertDAO simpleJdbcInsertDAO;

    public ProductDaoImpl(BatchUpdate batchUpdate, QueriesWithParameters queryWithParameters, SimpleJdbcInsertDAO simpleJdbcInsertDAO) {
        this.batchUpdate = batchUpdate;
        this.queryWithParameters = queryWithParameters;
        this.simpleJdbcInsertDAO = simpleJdbcInsertDAO;
    }

    @Override
    public List<Product> findByNameMap(String name) {
        return this.queryWithParameters.findByFullNameMap(name);
    }

    @Override
    public List<Product> findByNameBean(Product product) {
        return this.queryWithParameters.findByFullNameBean(product);
    }

    @Override
    public void initialize() {
        this.simpleJdbcInsertDAO.insert();
    }

    @Override
    public void initializeBatch(final List<Product> products) {
        this.batchUpdate.doUpdote(products);
    }
}
