package com.majumundur.majumundurshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.majumundur.majumundurshop.constant.ApiBash;
import com.majumundur.majumundurshop.model.request.TransactionRequest;
import com.majumundur.majumundurshop.model.response.TransactionResponse;
import com.majumundur.majumundurshop.service.TransactionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiBash.TRANSACTION)
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public TransactionResponse createNewTransaction(@RequestBody TransactionRequest request){
        return  transactionService.create(request);
    }

    @GetMapping
    public List<TransactionResponse> getAllTransactions(){
        return transactionService.getAll();
    }
}
