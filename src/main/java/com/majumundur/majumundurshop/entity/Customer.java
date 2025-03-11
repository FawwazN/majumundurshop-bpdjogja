package com.majumundur.majumundurshop.entity;

import com.majumundur.majumundurshop.constant.DbBash;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = DbBash.CUSTOMER_DB)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "mobile_phone")
    private String mobilePhone;
    @Column(name = "email")
    private String email;
    @Column(name = "address")
    private String address;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "points")
    private Integer points;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_account_id", referencedColumnName = "id")
    private UserAccount userAccount;
}
