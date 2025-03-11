package com.majumundur.majumundurshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.majumundur.majumundurshop.entity.TransactionDetail;
import com.majumundur.majumundurshop.repository.TransactionDetailRepository;
import com.majumundur.majumundurshop.service.TransactionDetailService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionDetailServiceImpl implements TransactionDetailService {

    private final TransactionDetailRepository transactionDetailRepository;

    @Override
    public List<TransactionDetail> createBulk(List<TransactionDetail> transactionDetails) {
        return transactionDetailRepository.saveAllAndFlush(transactionDetails);
    }
}
