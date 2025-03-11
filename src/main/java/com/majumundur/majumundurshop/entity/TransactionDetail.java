package com.majumundur.majumundurshop.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.majumundur.majumundurshop.constant.DbBash;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = DbBash.TRANSACTION_DETAIL_DB)
public class TransactionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "trx_id" , nullable = false)
    @JsonBackReference
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "product_id" , nullable = false)
    private Product product;

    @Column(name = "product_price" , updatable = false,nullable = false)
    private Long productPrice;
    
    @Column(name = "qty" , nullable = false)
    private Integer qty;
}
