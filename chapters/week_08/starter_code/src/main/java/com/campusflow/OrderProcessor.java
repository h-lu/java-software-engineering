package com.campusflow;

import java.util.List;

public class OrderProcessor {
    private final OrderRepository repository;

    public OrderProcessor(OrderRepository repository) {
        this.repository = repository;
    }

    public Order processOrder(String customerId, List<String> items, String paymentType,
                              String shippingMethod, boolean isVip, String couponCode) {
        // TODO: Refactor this long method. Keep the externally visible behavior protected by tests.
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("客户ID不能为空");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("订单不能为空");
        }

        double total = 0.0;
        for (String item : items) {
            double price = 0.0;
            if (item.equals("book")) {
                price = 50.0;
            } else if (item.equals("pen")) {
                price = 10.0;
            } else if (item.equals("notebook")) {
                price = 20.0;
            } else {
                price = 30.0;
            }
            total += price;
        }

        if (isVip) {
            total = total * 0.9;
        }

        if (couponCode != null && !couponCode.isEmpty()) {
            if (couponCode.equals("SAVE10")) {
                total = total * 0.9;
            } else if (couponCode.equals("SAVE20")) {
                total = total * 0.8;
            } else if (couponCode.equals("HALF")) {
                total = total * 0.5;
            }
        }

        double shippingCost = 0.0;
        if (shippingMethod.equals("express")) {
            shippingCost = 20.0;
            if (total > 200) {
                shippingCost = 0.0;
            }
        } else if (shippingMethod.equals("standard")) {
            shippingCost = 10.0;
            if (total > 100) {
                shippingCost = 0.0;
            }
        } else if (shippingMethod.equals("pickup")) {
            shippingCost = 0.0;
        }
        total += shippingCost;

        String paymentResult;
        if (paymentType.equals("credit_card")) {
            paymentResult = "信用卡支付: " + total;
        } else if (paymentType.equals("alipay")) {
            paymentResult = "支付宝支付: " + total;
        } else if (paymentType.equals("wechat")) {
            paymentResult = "微信支付: " + total;
        } else {
            paymentResult = "未知支付方式";
        }

        Order order = new Order(customerId, items, total, paymentType);
        repository.save(order);
        System.out.println("订单处理完成: " + paymentResult);
        return order;
    }
}
