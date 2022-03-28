package com.example.domain;

import java.math.BigDecimal;
import java.util.List;

public record CategorizedProduct (
        Long id, String name, String description, BigDecimal price, List<Category> categories){
}
