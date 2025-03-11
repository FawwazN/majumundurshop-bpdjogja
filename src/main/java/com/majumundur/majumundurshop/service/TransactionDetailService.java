package com.majumundur.majumundurshop.service;

import java.util.List;

import com.majumundur.majumundurshop.entity.TransactionDetail;

public interface TransactionDetailService {
    //method createBulk bertujuan untuk menyimpan banyak entitas kedalam database dalam sekali operasi
    List<TransactionDetail> createBulk(List<TransactionDetail> transactionDetails);
}
