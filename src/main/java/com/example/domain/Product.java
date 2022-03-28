package com.example.domain;

import java.math.BigDecimal;

public record Product(Long id, String name, String description, BigDecimal price) {
}
