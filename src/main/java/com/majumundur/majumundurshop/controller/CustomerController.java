package com.majumundur.majumundurshop.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.majumundur.majumundurshop.constant.ApiBash;
import com.majumundur.majumundurshop.constant.Constant;
import com.majumundur.majumundurshop.entity.Customer;
import com.majumundur.majumundurshop.model.request.NewCustomerRequest;
import com.majumundur.majumundurshop.model.request.SearchCustomerRequest;
import com.majumundur.majumundurshop.model.response.CustomerResponse;
import com.majumundur.majumundurshop.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping(path = ApiBash.CUSTOMER)
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

//    @PostMapping(consumes = {"multipart/form-data"})
//    @PreAuthorize("hasAnyRole('ADMIN')")
//    public ResponseEntity<CustomerResponse> createCustomer(@ModelAttribute NewCustomerRequest customer) {
//        CustomerResponse customerResponse =  customerService.create(customer);
//        return ResponseEntity.status(HttpStatus.CREATED).body(customerResponse);
//    }

    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasAnyRole('MERCHANT')")
    public ResponseEntity<CustomerResponse> createCustomer(
            @RequestParam("name") String name,
            @RequestParam("address") String address,
            @RequestParam("email") String email,
            @RequestParam("mobilePhone") String phone,
            @RequestParam("userName") String userName,
            @RequestParam("password") String password
    ) {
        NewCustomerRequest newCustomerRequest = NewCustomerRequest.builder()
                .name(name)
                .address(address)
                .email(email)
                .mobilePhone(phone)
                .username(userName)
                .password(password)
                .build();
        CustomerResponse customerResponse =  customerService.create(newCustomerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerResponse);
    }



    @GetMapping(ApiBash.GET_BY_ID)
    @PreAuthorize("hasAnyRole('MERCHANT')")
    public Customer getCustomerById(@PathVariable String id) {
        return customerService.getById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('MERCHANT')")
    public List<Customer> getAllCustomers(
            @RequestParam(name = "name" , required = false) String name,
            @RequestParam(name = "mobilePhone" , required = false) String phoneNumber,
            @RequestParam(name = "birthDate" , required = false) String birthDate,
            @RequestParam(name = "status" , required = false) Boolean status
    ) {
        SearchCustomerRequest request =  SearchCustomerRequest.builder()
                .name(name)
                .mobilePhoneNUmber(phoneNumber)
                .birthDate(birthDate)
                .status(status)
                .build();
        return customerService.getAll(request);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('MERCHANT')")
    public Customer updateCustomer(@RequestBody Customer customer){
        return customerService.update(customer);
    }

    @DeleteMapping(ApiBash.DELETED)
    @PreAuthorize("hasAnyRole('MERCHANT')")
    public String deleteCustomer(@PathVariable String id) {
        customerService.delete(id);
        return ApiBash.SUCCESS_DELETE_CUSTOMER;
    }

    @PutMapping(ApiBash.UPDATE)
    @PreAuthorize("hasAnyRole('MERCHANT')")
    public String updateStatusCustomer(
            @PathVariable(name = "id") String id,
            @RequestParam(name = "status") Boolean status
    ){
        customerService.updateStatusById(id, status);
        return Constant.SUCCESS_UPDATE_CUSTOMER;
    }
}