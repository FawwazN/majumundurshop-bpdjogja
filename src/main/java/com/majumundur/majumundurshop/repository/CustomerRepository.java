package com.majumundur.majumundurshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.majumundur.majumundurshop.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> , JpaSpecificationExecutor<Customer> {

    @Modifying
    @Query(value = "UPDATE m_customer SET status = :status WHERE id = :id" , nativeQuery = true)
    void updateStatus(@Param("id") String id, @Param("status") Boolean status);
    //CRUD -> Jpa Repository
    //Soft Deleted, Join , GroupBy , Having -> Native
}
