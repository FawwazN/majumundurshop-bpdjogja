package com.majumundur.majumundurshop.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.majumundur.majumundurshop.entity.Customer;
import com.majumundur.majumundurshop.entity.Product;
import com.majumundur.majumundurshop.entity.Transaction;
import com.majumundur.majumundurshop.entity.TransactionDetail;
import com.majumundur.majumundurshop.model.request.TransactionDetailRequest;
import com.majumundur.majumundurshop.model.request.TransactionRequest;
import com.majumundur.majumundurshop.model.response.TransactionResponse;
import com.majumundur.majumundurshop.repository.TransactionRepository;
import com.majumundur.majumundurshop.service.CustomerService;
import com.majumundur.majumundurshop.service.ProductService;
import com.majumundur.majumundurshop.service.TransactionDetailService;
import com.majumundur.majumundurshop.service.impl.TransactionServiceImpl;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private  TransactionDetailService transactionDetailService;
    @Mock
    private  CustomerService customerService;
    @Mock
    private  ProductService productService;

    private Customer customer;
    private Product product;
    private Transaction transaction;
    private TransactionDetail transactionDetail;
    private TransactionRequest transactionRequest;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id("cust-1")
                .name("Doni")
                .build();
        product = Product.builder()
                .id("prod-1")
                .name("laptop")
                .stock(10)
                .price(15000000L)
                .build();
        transaction = Transaction.builder()
                .id("trx-1")
                .customer(customer)
                .transDate(new Date())
                .build();
        transactionDetail = TransactionDetail.builder()
                .id("trx-detail-1")
                .transaction(transaction)
                .product(product)
                .qty(2)
                .productPrice(product.getPrice())
                .build();
        transactionRequest = TransactionRequest.builder()
                .customerId(customer.getId())
                .transactionDetail(List.of(
                        TransactionDetailRequest.builder()
                                .productId(product.getId())
                                .qty(2)
                                .build()
                ))
                .build();

    }

    @Test
    void create() {
        when(customerService.getById(customer.getId())).thenReturn(customer);
        when(productService.getById(product.getId())).thenReturn(product);
        when(transactionRepository.saveAndFlush(any(Transaction.class))).thenReturn(transaction);
        when(transactionDetailService.createBulk(anyList())).thenReturn(List.of(transactionDetail));

        TransactionResponse response = transactionService.create(transactionRequest);

        assertNotNull(response);
        assertEquals(customer.getId(), response.getCustomerId());

        verify(transactionDetailService, times(1)).createBulk(anyList());
        verify(transactionRepository, times(1)).saveAndFlush(any(Transaction.class));
    }

    @Test
    void getAll() {
        List<TransactionDetail> mockDetails = List.of(
                TransactionDetail.builder()
                        .id("trx-detail-1")
                        .transaction(transaction)
                        .product(product)
                        .qty(2)
                        .productPrice(product.getPrice())
                        .build()
        );

        transaction.setTransactionDetails(mockDetails);
        when(transactionRepository.findAll()).thenReturn(List.of(transaction));

        List<TransactionResponse> transactionResponses = transactionService.getAll();

        assertNotNull(transactionResponses);
        assertEquals(mockDetails.size(), transactionResponses.size());
        assertFalse(transactionResponses.isEmpty()); //false
        assertFalse(transactionResponses.get(0).getTransactionDetails().isEmpty()); //false
    }
}