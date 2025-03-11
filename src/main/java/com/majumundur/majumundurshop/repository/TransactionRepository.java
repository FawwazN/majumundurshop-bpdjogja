package com.majumundur.majumundurshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.majumundur.majumundurshop.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
