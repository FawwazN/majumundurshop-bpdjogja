package com.majumundur.majumundurshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.majumundur.majumundurshop.controller.ProductController;
import com.majumundur.majumundurshop.entity.Product;
import com.majumundur.majumundurshop.model.request.NewProductRequest;
import com.majumundur.majumundurshop.model.request.SearchProductRequest;
import com.majumundur.majumundurshop.model.response.CommonResponse;
import com.majumundur.majumundurshop.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); //menginit object yang ditandai dengan @Mock dan @InjectMock
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void createNewProduct() {
        //responseEntity
        NewProductRequest request = new NewProductRequest("Laptop", 100000L, 5);// hasil dari responsenya dari request
        Product mockProduct = new Product("1", "Laptop", 100000L, 5);//hasil dari datanya

        when(productService.create(any(NewProductRequest.class))).thenReturn(mockProduct);

        ResponseEntity<CommonResponse<Product>> response = productController.createNewProduct(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully Created Product", response.getBody().getMessage());
        assertEquals(mockProduct, response.getBody().getData());

        verify(productService, times(1)).create(any(NewProductRequest.class));
    }

    @Test
    void shouldCreateNewProductSuccessfully() throws Exception {
        Product mockProduct = new Product("1", "Laptop", 100000L, 5);

        when(productService.create(any(NewProductRequest.class))).thenReturn(mockProduct);

        mockMvc.perform(post("/api/v1/product")
                        .contentType("application/json")
                        .content("""
                                {
                                "name" : "Laptop",
                                "price" : 100000,
                                "stock" : 5
                                }
                                """))
                .andExpect(status().isCreated()) //harus mengembalikan 201
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.message").value("Successfully Created Product"))
                .andExpect(jsonPath("$.data.name").value("Laptop"))
                .andExpect(jsonPath("$.data.price").value(100000))
                .andExpect(jsonPath("$.data.stock").value(5));

        verify(productService, times(1)).create(any(NewProductRequest.class));

    }


    @Test
    void getProductById() {
        String productId = "1";
        Product mockProduct = new Product("1", "Laptop", 100000L, 5);

        when(productService.getById(productId)).thenReturn(mockProduct);

        ResponseEntity<Product> response = productController.getProductById(productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockProduct, response.getBody());

        verify(productService, times(1)).getById(productId);
    }

    @Test
    void getProductByIdMockMvc() throws Exception {
        String productId = "1";
        Product mockProduct = new Product("1", "Laptop", 100000L, 5);

        when(productService.getById(productId)).thenReturn(mockProduct);

        mockMvc.perform(get("/api/v1/product/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(100000))
                .andExpect(jsonPath("$.stock").value(5));

        verify(productService, times(1)).getById(productId);
    }


    @Test
    void updateProduct() {
        Product updateProduct = new Product("1", "Laptop", 100000L, 5);

        when(productService.update(any(Product.class))).thenReturn(updateProduct);

        ResponseEntity<Product> response = productController.updateProduct(updateProduct);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updateProduct, response.getBody());

        verify(productService, times(1)).update(any(Product.class));
    }

    @Test
    void updateProductMvc() throws Exception {
        //digunakan untuk konversi object ke JSON -> Membuat instance object baru dari objectmapper
        final ObjectMapper objectMapper = new ObjectMapper();

        Product updateProduct = new Product("1", "Laptop", 100000L, 5);

        when(productService.update(any(Product.class))).thenReturn(updateProduct);

        mockMvc.perform(put("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        //(objectMapper.writeValueAsString(updateProduct) -> mengubah object java menjadi json sebelum dikirim ke mockMvc.perform
                        .content(objectMapper.writeValueAsString(updateProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(100000))
                .andExpect(jsonPath("$.stock").value(5));

        verify(productService, times(1)).update(any(Product.class));
    }

    @Test
    void deleteById() {
        String productId = "1";

        doNothing().when(productService).deleteById(productId);

        ResponseEntity<String> response = productController.deleteById(productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(productService, times(1)).deleteById(productId);
    }

    @Test
    void deleteByIdMvc() throws Exception {
        String productId = "1";

        doNothing().when(productService).deleteById(productId);

       mockMvc.perform(delete("/api/v1/product/{id}", productId))
                       .andExpect(status().isOk());

        verify(productService, times(1)).deleteById(productId);
    }

    @Test
    void getAll() {
        Product product1 = new Product("1", "Laptop", 100000L, 5);
        Product product2 = new Product("2", "Keyboard", 10000L, 5);

        List<Product> productList = List.of(product1, product2);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<Product> mockPAge = new PageImpl<>(productList, pageable, productList.size());

        when(productService.getAll(any(SearchProductRequest.class))).thenReturn(mockPAge);

        ResponseEntity<CommonResponse<List<Product>>> response = productController.getAll(1, 10, "name", "asc", null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(productService, times(1)).getAll(any(SearchProductRequest.class));

    }

    @Test
    void getAllMockMvc() throws Exception {
        Product product1 = new Product("1", "Laptop", 100000L, 5);
        Product product2 = new Product("2", "Keyboard", 10000L, 5);

        List<Product> productList = List.of(product1, product2);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<Product> mockPAge = new PageImpl<>(productList, pageable, productList.size());

        when(productService.getAll(any(SearchProductRequest.class))).thenReturn(mockPAge);

        mockMvc.perform(get("/api/v1/product")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sortBy", "name")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.size()").value(2))
                .andExpect(jsonPath("$.data[0].name").value("Laptop"))
                .andExpect(jsonPath("$.data[1].name").value("Keyboard"));

        verify(productService, times(1)).getAll(any(SearchProductRequest.class));

    }
}