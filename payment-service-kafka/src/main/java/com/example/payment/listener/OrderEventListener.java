package com.example.payment.listener;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.event.OrderEvent;
import com.example.payment.entity.Payment;
import com.example.payment.repo.PaymentRepository;

@Service
public class OrderEventListener {
    @Autowired private KafkaTemplate<String, OrderEvent> kafka;
    @Autowired private PaymentRepository repo;

    @KafkaListener(topics = "order-events", groupId = "payment-group")
    public void onOrder(OrderEvent event) {
    	
    	System.out.println("✅ Received event onOrder: " + event);
    	if (!"ORDER_CREATED".equals(event.getEventType())) return;

        Payment payment = new Payment();
        payment.setOrderId(event.getOrderId());
        System.out.println("---------------------");
        boolean isPaid = new Random().nextBoolean(); // simulate
        if (isPaid) {
            payment.setStatus("SUCCESS");
            System.out.println("**************if*********");
            kafka.send("payment-events", new OrderEvent(event.getOrderId(), "PAYMENT_SUCCESS"));
        } else {
            payment.setStatus("FAILED");
            System.out.println("**************else*********");
            kafka.send("payment-events", new OrderEvent(event.getOrderId(), "PAYMENT_FAILED"));
        }

        repo.save(payment);
        System.out.println("✅ Order updated onOrder: " + payment);
        System.out.println();
    }
}