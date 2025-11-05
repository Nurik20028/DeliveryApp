package com.deliveryapp.backend.repository;

import com.deliveryapp.backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Integer> {

}
