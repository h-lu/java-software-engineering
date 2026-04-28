package com.campusflow;

import java.util.List;

public class Order {
    private final String customerId;
    private final List<String> items;
    private final double total;
    private final String paymentType;

    public Order(String customerId, List<String> items, double total, String paymentType) {
        this.customerId = customerId;
        this.items = List.copyOf(items);
        this.total = total;
        this.paymentType = paymentType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<String> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }

    public String getPaymentType() {
        return paymentType;
    }
}
