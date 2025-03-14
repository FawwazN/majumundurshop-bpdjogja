package com.majumundur.majumundurshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.majumundur.majumundurshop.entity.Product;
import com.majumundur.majumundurshop.model.request.NewProductRequest;
import com.majumundur.majumundurshop.model.request.SearchProductRequest;
import com.majumundur.majumundurshop.repository.ProductRepository;
import com.majumundur.majumundurshop.service.ProductService;
import com.majumundur.majumundurshop.specification.ProductSpecification;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public Product create(NewProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();
        return productRepository.saveAndFlush(product);
    }

    @Override
    public Product getById(String id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) throw new RuntimeException("product not found");
        return optionalProduct.get();
    }

    @Override
    public Page<Product> getAll(SearchProductRequest request) {
        if (request.getPage() <= 0 ) request.setPage(1);
        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of((request.getPage() - 1 ) , request.getSize() , sort);
        Specification<Product> specification = ProductSpecification.getSpecification(request);
        return productRepository.findAll(specification,pageable);
    }

    @Override
    public Product update(Product product) {
        getById(product.getId());
        return productRepository.saveAndFlush(product);
    }

    @Override
    public void deleteById(String id) {
        Product currentProduct = getById(id);
        productRepository.delete(currentProduct);
    }
}

