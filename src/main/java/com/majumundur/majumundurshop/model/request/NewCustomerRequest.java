package com.majumundur.majumundurshop.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewCustomerRequest {
    @NotBlank(message = "name is required")
    private String name;
    @NotBlank(message = "phone number is required")
    private String mobilePhone;
    @NotBlank(message = "email is required")
    private String email;
    private String address;
    @NotBlank(message = "username is required")
    private String username;
    @NotBlank(message = "password is required")
    private String password;
}
