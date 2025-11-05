package com.deliveryapp.backend.repository;

import com.deliveryapp.backend.model.Courier;
import  com.deliveryapp.backend.model.Order;
import  org.springframework.data.jpa.repository.JpaRepository;

public interface CourierRepository extends JpaRepository<Courier,Integer> {

}
