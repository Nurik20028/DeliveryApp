package com.deliveryapp.backend.dto.ordersDTO;
import com.deliveryapp.backend.enums.PaymentMethod;
import com.deliveryapp.backend.enums.TransportType;

public class OrderRegistrationRequest {
    private String number;
    private double point_a_latitude;
    private double point_a_longitude;
    private double point_b_latitude;
    private double point_b_longitude;
    private PaymentMethod payment_method;
    private TransportType transport_type;
    private String courier_comment;

    public void setPoint_a_latitude(double point_a_latitude) {
        this.point_a_latitude = point_a_latitude;
    }
    public double getPoint_a_latitude() {
        return point_a_latitude;
    }

    public void setPoint_a_longitude(double point_a_longitude) {
        this.point_a_longitude = point_a_longitude;
    }
    public double getPoint_a_longitude() {
        return point_a_longitude;
    }

    public void setPoint_b_latitude(double point_b_latitude) {
        this.point_b_latitude = point_b_latitude;
    }
    public double getPoint_b_latitude() {
        return point_b_latitude;
    }

    public void setPoint_b_longitude(double point_b_longitude) {
        this.point_b_longitude = point_b_longitude;
    }
    public double getPoint_b_longitude() {
        return point_b_longitude;
    }

    public void setPayment_method(PaymentMethod payment_method) {
        this.payment_method = payment_method;
    }
    public PaymentMethod getPayment_method() {
        return payment_method;
    }

    public void setTransport_type(TransportType transport_type) {
        this.transport_type = transport_type;
    }
    public TransportType getTransport_type() {
        return transport_type;
    }

    public void setCourier_comment(String courier_comment) {
        this.courier_comment = courier_comment;
    }
    public String getCourier_comment() {
        return courier_comment;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    public String getNumber() {
        return number;
    }
}
