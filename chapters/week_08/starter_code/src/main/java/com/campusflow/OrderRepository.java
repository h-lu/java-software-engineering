package com.campusflow;

import java.util.List;

public interface OrderRepository {
    void save(Order order);

    List<Order> findByCustomerId(String customerId);
}
