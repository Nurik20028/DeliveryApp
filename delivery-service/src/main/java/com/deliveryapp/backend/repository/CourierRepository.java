package com.deliveryapp.backend.repository;

import com.deliveryapp.backend.model.Courier;
import  org.springframework.data.jpa.repository.JpaRepository;

public interface CourierRepository extends JpaRepository<Courier,Integer> {

}
