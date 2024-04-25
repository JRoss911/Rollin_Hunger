package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class OrderRepositoryWithBagRelationshipsImpl implements OrderRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String ORDERS_PARAMETER = "orders";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Order> fetchBagRelationships(Optional<Order> order) {
        return order.map(this::fetchMenuItems);
    }

    @Override
    public Page<Order> fetchBagRelationships(Page<Order> orders) {
        return new PageImpl<>(fetchBagRelationships(orders.getContent()), orders.getPageable(), orders.getTotalElements());
    }

    @Override
    public List<Order> fetchBagRelationships(List<Order> orders) {
        return Optional.of(orders).map(this::fetchMenuItems).orElse(Collections.emptyList());
    }

    Order fetchMenuItems(Order result) {
        return entityManager
            .createQuery("select order from Order order left join fetch order.menuItems where order.id = :id", Order.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Order> fetchMenuItems(List<Order> orders) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, orders.size()).forEach(index -> order.put(orders.get(index).getId(), index));
        List<Order> result = entityManager
            .createQuery("select order from Order order left join fetch order.menuItems where order in :orders", Order.class)
            .setParameter(ORDERS_PARAMETER, orders)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
