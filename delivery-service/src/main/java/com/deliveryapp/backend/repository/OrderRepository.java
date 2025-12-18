package com.deliveryapp.backend.repository;

import com.deliveryapp.backend.enums.OrderStatus;
import com.deliveryapp.backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {
    // заказы, ожидающие курьера
    List<Order> findByOrderStatus(OrderStatus WAITING);

    // заказы конкретного пользователя
    List<Order> findByUser_Id(Integer userId);
}
