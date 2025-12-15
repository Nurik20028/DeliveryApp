package com.deliveryapp.backend.controller;

import com.deliveryapp.backend.dto.ordersDTO.OrderRegistrationRequest;
import com.deliveryapp.backend.dto.ordersDTO.OrderResponseDto;
import com.deliveryapp.backend.model.Order;
import com.deliveryapp.backend.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "*")
public class OrderCreatingController {
    private final OrderService orderService;

    public OrderCreatingController(OrderService orderService) {
        this.orderService = orderService;
    }
    @PostMapping("/create")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRegistrationRequest request, Principal principal) {
        String userPhoneNumber = principal.getName();

//      Order newOrder = orderService.registerOrder(request, userPhoneNumber);
        OrderResponseDto response = orderService.registerOrder(request, userPhoneNumber);
        return ResponseEntity.ok(response);
    }
}
