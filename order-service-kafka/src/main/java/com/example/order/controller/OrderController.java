package com.example.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.event.OrderEvent;
import com.example.order.entity.Order;
import com.example.order.repo.OrderRepository;
import com.example.order.service.OrderProducerService;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired 
    private OrderRepository orderRepo;
    @Autowired private OrderProducerService orderService;

    @PostMapping
    public String createOrder() {
        Order order = new Order();
        order.setStatus("PENDING");
        order = orderRepo.save(order);

        orderService.sendEvent(new OrderEvent(order.getId(), "ORDER_CREATED"));
        System.out.println("**********ORDER_CREATED");
        return "Order Created: " + order.getId();
    }
}