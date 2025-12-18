package com.deliveryapp.backend.service;

import com.deliveryapp.backend.dto.ordersDTO.OrderRegistrationRequest;
import com.deliveryapp.backend.dto.ordersDTO.OrderResponseDto;
import com.deliveryapp.backend.enums.OrderStatus;
import com.deliveryapp.backend.enums.TransportType;
import com.deliveryapp.backend.model.Courier;
import com.deliveryapp.backend.model.Order;
import com.deliveryapp.backend.model.User;
import com.deliveryapp.backend.repository.CourierRepository;
import com.deliveryapp.backend.repository.OrderRepository;
import com.deliveryapp.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private CourierRepository courierRepository;

    public OrderService(OrderRepository orderRepository, CourierRepository courierRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.courierRepository = courierRepository;
        this.userRepository = userRepository;
    }

    public OrderResponseDto registerOrder(OrderRegistrationRequest request, String userPhoneNumber) {
        Order newOrder = new Order();
        Instant now = Instant.now();

        User client = userRepository.findByPhoneNumber(userPhoneNumber);
        if (client == null) {
            return null;
        }

        newOrder.setPointALatitude(request.getPoint_a_latitude());
        newOrder.setPointALongitude(request.getPoint_a_longitude());
        newOrder.setPointBLatitude(request.getPoint_b_latitude());
        newOrder.setPointBLongitude(request.getPoint_b_longitude());

        newOrder.setUser(client);

        newOrder.setOrderStatus(OrderStatus.PENDING);
        newOrder.setCreatedAt(now);
//        newOrder.setDuration(); //нужно потом еще создать время завершения зависит от растояния
        newOrder.setPaymentMethod(request.getPayment_method());
        newOrder.setPaymentAmount(Amount(newOrder));
        newOrder.setCourierComment(request.getCourier_comment());
        newOrder.setTransportType(request.getTransport_type());

        Order savedOrder = orderRepository.save(newOrder);
        return convertToDto(savedOrder);
    }

    private OrderResponseDto convertToDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();

        // ВАЖНО: Если у вас ID в Order это Integer, используйте Long.valueOf
        dto.setId(Long.valueOf(order.getId()));

        dto.setOrderStatus(order.getOrderStatus());
        dto.setTransportType(order.getTransportType());
        dto.setAmount(order.getPaymentAmount());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setCreatedAt(order.getCreatedAt());

        dto.setPointALatitude(order.getPointALatitude());
        dto.setPointALongitude(order.getPointALongitude());
        dto.setPointBLatitude(order.getPointBLatitude());
        dto.setPointBLongitude(order.getPointBLongitude());

        // Безопасное добавление данных клиента
        if (order.getUser() != null) {
            dto.setClientId(Long.valueOf(order.getUser().getId()));
            dto.setClientName(order.getUser().getName());
            dto.setClientPhoneNumber(order.getUser().getPhoneNumber());
        }

        return dto;
    }

    public double distance (double a1,double a2,double b1, double b2){ //пока примерный расчет расстояния должен принять долготу и ширину точек А и B
        double R = 6371; // радиус Земли в км
        double deltaLat = Math.toRadians(a2 - a1);
        double deltaLon = Math.toRadians(b2 - b1);
        double lat1 = Math.toRadians(a1);
        double lat2 = Math.toRadians(a2);

        double a = Math.pow(Math.sin(deltaLat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(deltaLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c; // расстояние в километрах

        return d;
    }
    // 👇 Упрощенный метод: убираем зависимость от 'number'
    public BigDecimal Amount(Order order) {

        // Рассчитываем только расстояние от А до Б
        double distanceAB = distance(order.getPointALatitude(),
                order.getPointBLatitude(),
                order.getPointALongitude(),
                order.getPointBLongitude());

        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal bdDistance = BigDecimal.valueOf(distanceAB);

        // Задаем базовый тариф (например, 100) и цену за километр (например, 50)
        int baseRate = 100;
        int ratePerKm = 50;

        // Логика тарифа по типу транспорта
        if (TransportType.BIKE.equals(order.getTransportType())) {
            baseRate = 100; ratePerKm = 50;
        } else if (TransportType.CAR.equals(order.getTransportType())) {
            baseRate = 150; ratePerKm = 80;
        } else if (TransportType.TRUCK.equals(order.getTransportType())) {
            baseRate = 200; ratePerKm = 100;
        }

        // Расчет: (Расстояние * Цена за км) + Базовый тариф
        amount = bdDistance.multiply(BigDecimal.valueOf(ratePerKm))
                .add(BigDecimal.valueOf(baseRate));

        // Устанавливаем масштаб (два знака после запятой)
        return amount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    // функция связывает заказ с курьером (срабатывает когда курьер примет заказ)
    public Order acceptOrder(Integer orderId, Integer courierId) {

        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new RuntimeException("Courier not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setCourier(courier);
        order.setOrderStatus(OrderStatus.WAITING);

        return orderRepository.save(order);
    }
}
