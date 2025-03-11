package com.majumundur.majumundurshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.majumundur.majumundurshop.constant.UserRole;
import com.majumundur.majumundurshop.entity.Customer;
import com.majumundur.majumundurshop.entity.UserAccount;
import com.majumundur.majumundurshop.model.request.NewCustomerRequest;
import com.majumundur.majumundurshop.model.request.SearchCustomerRequest;
import com.majumundur.majumundurshop.model.response.CustomerResponse;
import com.majumundur.majumundurshop.repository.CustomerRepository;
import com.majumundur.majumundurshop.repository.UserAccountRepository;
import com.majumundur.majumundurshop.service.CustomerService;
import com.majumundur.majumundurshop.specification.CustomerSpecification;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public CustomerResponse create(NewCustomerRequest request) {

        UserRole userRole = UserRole.ROLE_USER;

        UserAccount userAccount = UserAccount.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(userRole)
            .build();
        userAccountRepository.saveAndFlush(userAccount);
        Customer customer = Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .mobilePhone(request.getMobilePhone())
                .address(request.getAddress())
                .userAccount(userAccount)
                .points(0)
                .status(true)
                .build();
        customerRepository.saveAndFlush(customer);
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phoneNumber(customer.getMobilePhone())
                .username(userAccount.getUsername())
                .role(userAccount.getRole().getDescription())
                .build();
    }

    @Override
    public Customer getById(String id) {
        //cara 1
//        Optional<Customer> customer = customerRepository.findById(id);
//        if (customer.isEmpty()) throw new RuntimeException("Customer not found");
//        return customer.get();

        //cara 2
//        return customerRepository.findById(id).orElse(null);

        //cara 3
        return findByIdOrThrowNotFound(id);
    }

    @Override
    public List<Customer> getAll(SearchCustomerRequest request) {
        Specification<Customer> specification = CustomerSpecification.getSpecification(request);
        return customerRepository.findAll(specification);
    }

    @Override
    public Customer update(Customer customer) {
        findByIdOrThrowNotFound(customer.getId());
        return customerRepository.saveAndFlush(customer);
    }

    @Override
    public void delete(String id) {
        Customer customer = findByIdOrThrowNotFound(id);
        customerRepository.delete(customer);
    }

    @Override
    public void updateStatusById(String id, Boolean status) {
        findByIdOrThrowNotFound(id);
        customerRepository.updateStatus(id, status);
    }

    public Customer findByIdOrThrowNotFound(String id) {
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("customer not found"));
    }

}
