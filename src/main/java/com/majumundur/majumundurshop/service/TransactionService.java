package com.majumundur.majumundurshop.service;

import java.util.List;

import com.majumundur.majumundurshop.model.request.TransactionRequest;
import com.majumundur.majumundurshop.model.response.TransactionResponse;

public interface TransactionService {
    TransactionResponse create(TransactionRequest request);
    List<TransactionResponse> getAll();
}
