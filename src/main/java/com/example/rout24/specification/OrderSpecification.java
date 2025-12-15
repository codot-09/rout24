package com.example.rout24.specification;

import com.example.rout24.entity.Order;
import com.example.rout24.entity.enums.OrderStatus;
import com.example.rout24.entity.enums.Regions;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {

    public static Specification<Order> filter(
            String clientChatId,
            Regions from,
            Regions to,
            OrderStatus status
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (clientChatId != null) {
                predicates.add(cb.equal(root.get("client").get("chatId"), clientChatId));
            }

            if (from != null) {
                predicates.add(cb.equal(root.get("route").get("fromRegion"), from));
            }

            if (to != null) {
                predicates.add(cb.equal(root.get("route").get("toRegion"), to));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
