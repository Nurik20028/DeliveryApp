package com.deliveryapp.backend.repository;

import com.deliveryapp.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByPhoneNumber(String phoneNumber);
}
