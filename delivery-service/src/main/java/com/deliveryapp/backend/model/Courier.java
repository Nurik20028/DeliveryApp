package com.deliveryapp.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.deliveryapp.backend.enums.TransportType;
import com.deliveryapp.backend.enums.CourierStatus;

@Getter
@Setter
@Entity
@Table(name = "courier")
public class Courier {
    @OneToMany(mappedBy = "courier",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<Order> orders = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "transport_type", nullable = false, length = Integer.MAX_VALUE)
    private TransportType transportType;

    @Size(max = 10)
    @Column(name = "transoprt_number", length = 10)
    private String transportNumber;

    @Column(name = "courier_latitude")
    private Double courierLatitude;

    @Column(name = "courier_longitude")
    private Double courierLongitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "courier_status", length = Integer.MAX_VALUE)
    private CourierStatus courierStatus;

    @ColumnDefault("0")
    @Column(name = "courier_balance", precision = 10, scale = 2)
    private BigDecimal courierBalance;

    @Size(max = 20)
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Size(max = 100)
    @NotNull
    @Column(name = "password", nullable = false, length = 100)
    private String password;

}