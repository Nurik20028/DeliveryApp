package com.deliveryapp.backend.model;

import com.deliveryapp.backend.enums.OrderStatus;
import com.deliveryapp.backend.enums.PaymentMethod;
import com.deliveryapp.backend.enums.TransportType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    private Courier courier;


    @Enumerated(EnumType.STRING)
    @Column(name = "transport_type", length = Integer.MAX_VALUE)
    private TransportType transportType;

    @Column(name = "point_a_latitude")
    private Double pointALatitude;

    @Column(name = "point_a_longitude")
    private Double pointALongitude;

    @Column(name = "point_b_latitude")
    private Double pointBLatitude;

    @Column(name = "point_b_longitude")
    private Double pointBLongitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", length = Integer.MAX_VALUE)
    private OrderStatus orderStatus;

    @ColumnDefault("now()")
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "duration")
    private LocalTime duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = Integer.MAX_VALUE)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_amount", precision = 10, scale = 2)
    private BigDecimal paymentAmount;

    @Column(name = "courier_comment", length = Integer.MAX_VALUE)
    private String courierComment;

}