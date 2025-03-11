package com.majumundur.majumundurshop.service;

import org.springframework.data.domain.Page;

import com.majumundur.majumundurshop.entity.Product;
import com.majumundur.majumundurshop.model.request.NewProductRequest;
import com.majumundur.majumundurshop.model.request.SearchProductRequest;

public interface ProductService {
    Product create(NewProductRequest product);
    Product getById(String id);
    Page<Product> getAll(SearchProductRequest request);
    Product update(Product product);
    void deleteById(String id);
}
