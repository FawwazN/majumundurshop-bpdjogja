package com.majumundur.majumundurshop.model.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchProductRequest {
    private Integer page;
    private Integer size;
    private String sortBy;
    private String direction;  // ASC/DESC
    private String name;
}
