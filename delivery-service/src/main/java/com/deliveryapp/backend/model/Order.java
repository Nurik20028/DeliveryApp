package com.deliveryapp.backend.model;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "courier_id")
    private Courier courier;

    @Column(name = "transport_type", length = Integer.MAX_VALUE)
    private String transportType;

    @Column(name = "point_a_latitude")
    private Double pointALatitude;

    @Column(name = "point_a_longitude")
    private Double pointALongitude;

    @Column(name = "point_b_latitude")
    private Double pointBLatitude;

    @Column(name = "point_b_longitude")
    private Double pointBLongitude;

    @Column(name = "order_status", length = Integer.MAX_VALUE)
    private String orderStatus;

    @ColumnDefault("now()")
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "duration")
    private LocalTime duration;

    @Column(name = "payment_method", length = Integer.MAX_VALUE)
    private String paymentMethod;

    @Column(name = "payment_amount", precision = 10, scale = 2)
    private BigDecimal paymentAmount;

    @Column(name = "courier_comment", length = Integer.MAX_VALUE)
    private String courierComment;

}