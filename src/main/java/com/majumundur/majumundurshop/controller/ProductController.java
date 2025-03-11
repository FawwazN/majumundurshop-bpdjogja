package com.majumundur.majumundurshop.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.majumundur.majumundurshop.constant.ApiBash;
import com.majumundur.majumundurshop.entity.Product;
import com.majumundur.majumundurshop.model.request.NewProductRequest;
import com.majumundur.majumundurshop.model.request.SearchProductRequest;
import com.majumundur.majumundurshop.model.response.CommonResponse;
import com.majumundur.majumundurshop.model.response.PagingResponse;
import com.majumundur.majumundurshop.service.ProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Product API" , description = "API Untuk mengelolah product")
@RequestMapping(path = ApiBash.PRODUCT)
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAnyRole('MERCHANT')")
    public ResponseEntity<CommonResponse<Product>> createNewProduct(@RequestBody NewProductRequest request) {
        Product newProduct = productService.create(request);
        CommonResponse<Product> response = CommonResponse.<Product>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully Created Product")
                .data(newProduct)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED) .body(response);
    }

    @GetMapping(path = ApiBash.GET_BY_ID)
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Product product = productService.getById(id);
        return ResponseEntity.ok(product);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('MERCHANT')")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
        Product updatedProduct = productService.update(product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping(ApiBash.DELETED)
    @PreAuthorize("hasAnyRole('MERCHANT')")
    public ResponseEntity<String> deleteById(@PathVariable String id) {
        productService.deleteById(id);
        return ResponseEntity.ok("Successfully delete product");
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<Product>>> getAll(
            @RequestParam(name = "page" , defaultValue = "1") Integer page,
            @RequestParam(name = "size" , defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy" ,defaultValue = "name") String sortBy,
            @RequestParam(name = "direction" , defaultValue = "asc") String direction,
            @RequestParam(name = "name" , required = false) String name
    ) {
        SearchProductRequest request = SearchProductRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .name(name)
                .build();
        Page<Product> products = productService.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(products.getTotalPages())
                .totalElement(products.getTotalElements())
                .page(products.getPageable().getPageNumber() + 1)
                .size(products.getPageable().getPageSize() + 1)
                .hasNext(products.hasNext())
                .hasPrevious(products.hasPrevious())
                .build();

        CommonResponse<List<Product>> response = CommonResponse.<List<Product>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success Get All Product")
                .data(products.getContent())
                .paging(pagingResponse)
                .build();
        return ResponseEntity.ok(response);
    }
}

