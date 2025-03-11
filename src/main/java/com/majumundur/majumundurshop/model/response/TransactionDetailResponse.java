package com.majumundur.majumundurshop.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDetailResponse {
    private String id;
    private String productId;
    private Long productPrice;
    private Integer quantity;
}
