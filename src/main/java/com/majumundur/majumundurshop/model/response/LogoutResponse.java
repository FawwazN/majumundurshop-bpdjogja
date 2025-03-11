package com.majumundur.majumundurshop.model.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogoutResponse {
    private String message;
    private String token;
    private Long expiredIn;
}
