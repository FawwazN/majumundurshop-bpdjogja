package com.majumundur.majumundurshop.model.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String username;
    private String role;
}
