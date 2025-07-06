package com.example.order.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.event.OrderEvent;
import com.example.order.entity.Order;
import com.example.order.repo.OrderRepository;

@Service
public class PaymentEventListener {
    @Autowired 
    private OrderRepository repo;

    @KafkaListener(topics = "payment-events", groupId = "order-group")
    public void listen(OrderEvent event) {
    	System.out.println("✅ Received event: " + event);
    	Order order = repo.findById(event.getOrderId()).orElseThrow(() -> new RuntimeException("Order not found: " + event.getOrderId()));
        if ("PAYMENT_SUCCESS".equals(event.getEventType())) {
            order.setStatus("COMPLETED");
        } else {
            order.setStatus("CANCELLED"); // Compensation
        }
        repo.save(order);
        System.out.println("✅ Order updated: " + order);
    }
}
