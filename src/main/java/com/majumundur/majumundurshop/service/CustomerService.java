package com.majumundur.majumundurshop.service;

import java.util.List;

import com.majumundur.majumundurshop.entity.Customer;
import com.majumundur.majumundurshop.model.request.NewCustomerRequest;
import com.majumundur.majumundurshop.model.request.SearchCustomerRequest;
import com.majumundur.majumundurshop.model.response.CustomerResponse;

public interface CustomerService {
    CustomerResponse create(NewCustomerRequest customer);
    Customer getById(String id);
    List<Customer> getAll(SearchCustomerRequest request);
    Customer update(Customer customer);
    void delete(String id);
    void updateStatusById(String id , Boolean status);
}
