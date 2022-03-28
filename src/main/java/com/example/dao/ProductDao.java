package com.example.dao;

import com.example.domain.Product;

import java.util.List;

public interface ProductDao {

    List<Product> findByNameMap(String name);
    List<Product> findByNameBean(Product product);

    void initialize();
    void initializeBatch(final List<Product> products);

}
