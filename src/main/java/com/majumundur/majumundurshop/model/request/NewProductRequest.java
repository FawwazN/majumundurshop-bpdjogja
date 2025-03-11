package com.majumundur.majumundurshop.model.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewProductRequest {
    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "price is required")
    @Min(value = 0 , message = "price must be greater than or equal 0")
    private Long price;

    @NotBlank(message = "stock is required")
    @Min(value = 0 , message = "stock must be greater than or equal 0")
    private Integer stock;
}
