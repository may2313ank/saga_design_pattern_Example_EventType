package com.example.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.event.OrderEvent;

@Service
public class OrderProducerService {
    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void sendEvent(OrderEvent event) {
        kafkaTemplate.send("order-events", event);
    }
}