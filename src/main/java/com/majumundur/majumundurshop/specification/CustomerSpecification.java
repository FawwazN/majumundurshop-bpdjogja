package com.majumundur.majumundurshop.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import com.majumundur.majumundurshop.entity.Customer;
import com.majumundur.majumundurshop.model.request.SearchCustomerRequest;
import com.majumundur.majumundurshop.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerSpecification {
    public static Specification<Customer> getSpecification(SearchCustomerRequest request){
        return (root,cq,cb) ->{
            /*
            * 3 Object Creteria yang digunakan
            * 1. Creteria BUilder -> unutk membangun query expresion ( < , > ,like ,<> , != ) dan membangun query ,update ,delete
            * 2. creteria (Query(select) ,Update ,Delete) -> where , orderBy , having , groupBy
            * 3. Root ->  untuk mempresentasikan dari entity
             */

            List<Predicate> predicates = new ArrayList<>();

            if (request.getName() != null){
                Predicate namePredicate = cb.like(cb.lower(root.get("name")), "%" + request.getName().toLowerCase() +" %");
                predicates.add(namePredicate);
            }

            if (request.getMobilePhoneNUmber() != null){
                Predicate phonePredicate = cb.equal(cb.lower(root.get("mobilePhone")), request.getMobilePhoneNUmber());
                predicates.add(phonePredicate);
            }

            if (request.getStatus() != null){
                Predicate statusPredicate = cb.equal(cb.lower(root.get("status")), request.getStatus());
                predicates.add(statusPredicate);
            }

            if (request.getBirthDate() != null){
                Date tempDate = DateUtil.parseDate(request.getBirthDate(), "yyyy-MM-dd");
                Predicate datePredicate = cb.equal(root.get("birthDate") , tempDate);
                predicates.add(datePredicate);
            }

            assert cq !=null;
            return cq.where(predicates.toArray(new Predicate[]{})).getRestriction();

        };

    }
}
