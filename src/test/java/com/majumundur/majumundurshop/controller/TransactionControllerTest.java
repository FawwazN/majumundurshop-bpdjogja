package com.majumundur.majumundurshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.majumundur.majumundurshop.controller.TransactionController;
import com.majumundur.majumundurshop.model.request.TransactionDetailRequest;
import com.majumundur.majumundurshop.model.request.TransactionRequest;
import com.majumundur.majumundurshop.model.response.TransactionResponse;
import com.majumundur.majumundurshop.service.TransactionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private TransactionRequest transactionRequest;
    private TransactionResponse transactionResponse;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();

        transactionRequest = new TransactionRequest();
        transactionRequest.setCustomerId("customer-1");
        transactionRequest.setTransactionDetail(List.of(
                new TransactionDetailRequest("product-1", 2),
                new TransactionDetailRequest("product-2", 2)
        ));

        transactionResponse = new TransactionResponse();
        transactionResponse.setId("trx-1");
        transactionResponse.setCustomerId("customer-1");
        transactionResponse.setTransactionDetails(Collections.emptyList());
    }

    @Test
    void createNewTransaction() throws Exception {
        when(transactionService.create(any(TransactionRequest.class))).thenReturn(transactionResponse);

        mockMvc.perform(post("/api/v1/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("trx-1"))
                .andExpect(jsonPath("$.customerId").value("customer-1"));

        verify(transactionService, times(1)).create(any(TransactionRequest.class));
    }

    @Test
    void getAllTransactions() throws Exception {
        when(transactionService.getAll()).thenReturn(List.of(transactionResponse));

        mockMvc.perform(get("/api/v1/transactions"))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value("trx-1"));

        verify(transactionService, times(1)).getAll();
    }
}