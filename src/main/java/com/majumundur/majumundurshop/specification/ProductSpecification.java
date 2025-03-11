package com.majumundur.majumundurshop.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import com.majumundur.majumundurshop.entity.Product;
import com.majumundur.majumundurshop.model.request.SearchProductRequest;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> getSpecification(SearchProductRequest request){
        return (root,cq,cb) ->{
            List<Predicate> predicates = new ArrayList<>();

            if (request.getName() != null){
                predicates.add(
                        cb.like(cb.lower(root.get("name")), "%"+request.getName().toLowerCase()+"%")
                );
            }
            assert cq != null;
            return cq.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

    }
}
