package com.deliveryapp.backend.service;

import com.deliveryapp.backend.enums.OrderStatus;
import com.deliveryapp.backend.enums.PaymentMethod;
import com.deliveryapp.backend.enums.TransportType;
import com.deliveryapp.backend.model.Courier;
import com.deliveryapp.backend.model.Order;
import com.deliveryapp.backend.model.User;
import com.deliveryapp.backend.repository.CourierRepository;
import com.deliveryapp.backend.repository.OrderRepository;
import com.deliveryapp.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CourierRepository courierRepository;

    // -------------------------------
    // 1. Создание заказа
    // -------------------------------
    public Order createOrder(Integer userId,
                             Double pointALat,
                             Double pointALng,
                             Double pointBLat,
                             Double pointBLng) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);

        order.setPointALatitude(pointALat);
        order.setPointALongitude(pointALng);
        order.setPointBLatitude(pointBLat);
        order.setPointBLongitude(pointBLng);

        order.setCreatedAt(Instant.now());
        order.setOrderStatus(OrderStatus.WAITING);
        order.setTransportType(TransportType.BIKE); // или BIKE, или SCOOTER
        order.setPaymentMethod(PaymentMethod.CASH); // или другое
        order.setPaymentAmount(BigDecimal.valueOf(0));

        return orderRepository.save(order);
    }

    // -------------------------------
    // 2. Курьер принимает заказ
    // -------------------------------
    public Order acceptOrder(Integer orderId, Integer courierId) {

        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new RuntimeException("Courier not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setCourier(courier);
        order.setOrderStatus(OrderStatus.ACCEPTED);

        return orderRepository.save(order);
    }
}
