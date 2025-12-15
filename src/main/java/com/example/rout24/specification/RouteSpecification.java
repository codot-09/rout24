package com.example.rout24.specification;

import com.example.rout24.entity.Route;
import com.example.rout24.entity.User;
import com.example.rout24.entity.enums.Regions;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RouteSpecification {

    public static Specification<Route> filterByDriver(User driver, Regions from, Regions to) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("driver"), driver));

            if (from != null) {
                predicates.add(cb.equal(root.get("fromRegion"), from));
            }

            if (to != null) {
                predicates.add(cb.equal(root.get("toRegion"), to));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Route> globalFilter(
            Regions from,
            Regions to,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            LocalDate departureDate
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("finished")));

            if (from != null) {
                predicates.add(cb.equal(root.get("fromRegion"), from));
            }

            if (to != null) {
                predicates.add(cb.equal(root.get("toRegion"), to));
            }

            if (minPrice != null) {
                predicates.add(cb.ge(root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(cb.le(root.get("price"), maxPrice));
            }

            if (departureDate != null) {
                predicates.add(cb.equal(root.get("departureDate"), departureDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
