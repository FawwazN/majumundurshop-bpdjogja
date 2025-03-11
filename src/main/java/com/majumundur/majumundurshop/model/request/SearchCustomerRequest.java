package com.majumundur.majumundurshop.model.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchCustomerRequest {
    private String name ;
    private String mobilePhoneNUmber;
    private String birthDate;
    private Boolean status;
}
