package com.majumundur.majumundurshop.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import com.majumundur.majumundurshop.entity.Product;
import com.majumundur.majumundurshop.model.request.NewProductRequest;
import com.majumundur.majumundurshop.model.request.SearchProductRequest;
import com.majumundur.majumundurshop.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private NewProductRequest newProductRequest;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id("1")
                .name("Test Product")
                .price(10000L)
                .stock(10)
                .build();

        newProductRequest = NewProductRequest.builder()
                .name("New Product")
                .price(10000L)
                .stock(10)
                .build();
    }

    @Test
    void shouldCreateProductSuccessfully() {
        //when -> when(...).return(...) -> untuk mensimulasikan prilaku
        when(productRepository.saveAndFlush(any(Product.class))).thenReturn(product);

        //memanggil method di service yaitu methid create
        Product createProduct = productService.create(newProductRequest);

        //memastikan apakah object createProduct tidak null
        assertNotNull(createProduct);

        //membandingkan
        assertEquals("Test Product" , createProduct.getName());
        assertEquals(10000L, createProduct.getPrice());
        assertEquals(10, createProduct.getStock());

        //memeriksa apakah method saveAndFlush dipanggil sekali
        verify(productRepository , times(1)).saveAndFlush(any(Product.class));
    }

    @Test
    void shouldReturnProductWhenIdExists() {
        when(productRepository.findById("1")).thenReturn(Optional.of(product));

        Product foundProduct = productService.getById("1");

        assertNotNull(foundProduct);
        assertEquals("Test Product" , foundProduct.getName());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound(){
        when(productRepository.findById("1")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> productService.getById("2"));
    }

    @Test
    void shouldThrowExceptionWhenDeletedNonExistingProduct(){
        when(productRepository.findById("1")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> productService.deleteById("2"));
    }

    @Test
    void shouldReturnProductPageWhenGetAllSuccessfully() {
        //Arrange
        SearchProductRequest searchProductRequest = new SearchProductRequest();
        searchProductRequest.setPage(1);
        searchProductRequest.setSize(10);
        searchProductRequest.setSortBy("name");
        searchProductRequest.setDirection("ASC");

        //MockData
        Product product1 = new Product("1" , "Product A" , 100000L,10);
        Page<Product> mockPage = new PageImpl<>(List.of(product1));

        Sort sort = Sort.by(Sort.Direction.ASC, searchProductRequest.getSortBy());
        Pageable pageable = PageRequest.of(0,10,sort);// pastikan dimulai dari 0

        when(productRepository.findAll(any(Specification.class) , eq(pageable))).thenReturn(mockPage);

        Page<Product> resul = productService.getAll(searchProductRequest);

        assertEquals(1, resul.getTotalElements()); // memastikan hanya ada 1 product yang dibalikan

        verify(productRepository, times(1)).findAll(any(Specification.class) , eq(pageable));
    }

    @Test
    void shouldUpdateProductSuccessfully() {
        when(productRepository.findById("1")).thenReturn(Optional.of(product));
        when(productRepository.saveAndFlush(any(Product.class))).thenReturn(product);

        Product updateProduct = productService.update(product);

        assertNotNull(updateProduct);
        assertEquals("Test Product" , updateProduct.getName());

        verify(productRepository , times(1)).saveAndFlush(any(Product.class));
        verify(productRepository, times(1)).findById("1");
    }

    @Test
    void deleteById() {
        when(productRepository.findById("1")).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        productService.deleteById("1");

        verify(productRepository, times(1)).delete(product);
    }
};