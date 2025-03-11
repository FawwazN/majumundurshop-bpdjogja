package com.majumundur.majumundurshop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.majumundur.majumundurshop.constant.DbBash;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = DbBash.TRANSACTION_DB)
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany
    @JsonBackReference
    private List<TransactionDetail> transactionDetails;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "trans_date" , updatable = false)
    private Date transDate;
}
