package com.majumundur.majumundurshop.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.majumundur.majumundurshop.entity.Customer;
import com.majumundur.majumundurshop.entity.Product;
import com.majumundur.majumundurshop.entity.Transaction;
import com.majumundur.majumundurshop.entity.TransactionDetail;
import com.majumundur.majumundurshop.model.request.TransactionRequest;
import com.majumundur.majumundurshop.model.response.TransactionDetailResponse;
import com.majumundur.majumundurshop.model.response.TransactionResponse;
import com.majumundur.majumundurshop.repository.TransactionRepository;
import com.majumundur.majumundurshop.service.CustomerService;
import com.majumundur.majumundurshop.service.ProductService;
import com.majumundur.majumundurshop.service.TransactionDetailService;
import com.majumundur.majumundurshop.service.TransactionService;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionDetailService transactionDetailService;
    private final CustomerService customerService;
    private final ProductService productService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransactionResponse create(TransactionRequest request) {
        //1. cari / validasi customer
        Customer customer = customerService.getById(request.getCustomerId());

        //2. save transaction
        Transaction trx = Transaction.builder()
                .customer(customer)
                .transDate(new Date())
                .build();
        transactionRepository.saveAndFlush(trx);

        //3. save transactionDetail
        //java stream
        List<TransactionDetail> trxDetail = request.getTransactionDetail().stream().map(detailRequest -> {

            Product product = productService.getById(detailRequest.getProductId());
            if (product.getStock() - detailRequest.getQty() < 0) throw new RuntimeException("Sold Out");

            product.setStock(product.getStock() - detailRequest.getQty());

            if (detailRequest.getQty() > 5) {
                customer.setPoints(customer.getPoints() + 40);
            } else if (detailRequest.getQty() > 3) {
                customer.setPoints(customer.getPoints() + 20);
            }

            return TransactionDetail.builder()
                    .product(product)
                    .transaction(trx)
                    .qty(detailRequest.getQty())
                    .productPrice(product.getPrice())
                    .build();
        }).toList();

        transactionDetailService.createBulk(trxDetail);
        trx.setTransactionDetails(trxDetail);

        List<TransactionDetailResponse> trxDetailResponses = getTransactionDetailResponses(trxDetail);
        return getTransactionResponse(trx, customer, trxDetailResponses);
    }

    @Override
    public List<TransactionResponse> getAll() {
        List<Transaction> transactions = transactionRepository.findAll();

        return transactions.stream().map(trx -> {
            List<TransactionDetailResponse> trxDetailResponse = getTransactionDetailResponses(trx.getTransactionDetails());

            return getTransactionResponse(trx, trx.getCustomer(), trxDetailResponse);
        }).toList();
    }

    private static List<TransactionDetailResponse> getTransactionDetailResponses(List<TransactionDetail> trxDetail) {
        List<TransactionDetailResponse> trxDetailResponses = trxDetail.stream().map(detail -> {
            return TransactionDetailResponse.builder()
                    .id(detail.getId())
                    .productId(detail.getProduct().getId())
                    .productPrice(detail.getProductPrice())
                    .quantity(detail.getQty())
                    .build();
        }).toList();
        return trxDetailResponses;
    }


    private static TransactionResponse getTransactionResponse(Transaction trx, Customer customer, List<TransactionDetailResponse> trxDetailResponses) {
        return TransactionResponse.builder()
                .id(trx.getId())
                .customerId(customer.getId())
                .transDate(trx.getTransDate())
                .transactionDetails(trxDetailResponses)
                .build();
    }
}