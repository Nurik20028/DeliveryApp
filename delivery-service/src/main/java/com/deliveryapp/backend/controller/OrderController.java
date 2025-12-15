package com.deliveryapp.backend.controller;

import com.deliveryapp.backend.enums.OrderStatus;
import com.deliveryapp.backend.model.Order;
import com.deliveryapp.backend.service.OrderService;
import com.deliveryapp.backend.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    // -------------------------------
    // 1. Создание заказа пользователем
    // -------------------------------
    @PostMapping("/create")
    public Order createOrder(
            @RequestParam Integer userId,
            @RequestParam Double pointALat,
            @RequestParam Double pointALng,
            @RequestParam Double pointBLat,
            @RequestParam Double pointBLng
    ) {
        return orderService.createOrder(userId, pointALat, pointALng, pointBLat, pointBLng);
    }

    // -------------------------------
    // 2. Заказы, ожидающие курьера
    // -------------------------------
    @GetMapping("/waiting")
    public List<Order> getWaitingOrders() {
        return orderRepository.findByOrderStatus(OrderStatus.WAITING);
    }

    // -------------------------------
    // 3. Получить заказы пользователя
    // -------------------------------
    @GetMapping("/user/{userId}")
    public List<Order> getUserOrders(@PathVariable Integer userId) {
        return orderRepository.findByUser_Id(userId);
    }

    // -------------------------------
    // 4. Курьер принимает заказ
    // -------------------------------
    @PostMapping("/{orderId}/accept")
    public Order acceptOrder(
            @PathVariable Integer orderId,
            @RequestParam Integer courierId
    ) {
        return orderService.acceptOrder(orderId, courierId);
    }
}
