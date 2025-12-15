package com.deliveryapp.backend.enums;

public enum OrderStatus {
    PENDING,        // заказ создан
    WAITING,        // ожидает курьера
    ACCEPTED,       // курьер принял заказ
    ON_THE_WAY,     // курьер в пути
    DELIVERED       // заказ доставлен
}
