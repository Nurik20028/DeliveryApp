import React, { useEffect, useState } from "react";
import axios from "axios";

export default function CourierHomePage({ user }) {
    const [waitingOrders, setWaitingOrders] = useState([]);
    const [isActive, setIsActive] = useState(true);

    const token = localStorage.getItem("token");

    useEffect(() => {
        if (isActive) {
            loadWaitingOrders();
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [isActive]);

    const loadWaitingOrders = async () => {
        try {
            const response = await axios.get(
                "http://localhost:8080/api/orders/waiting",
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );
            setWaitingOrders(response.data);
        } catch (error) {
            console.error("Ошибка загрузки заказов:", error);
        }
    };

    const acceptOrder = async (orderId) => {
        try {
            await axios.post(
                `http://localhost:8080/api/orders/${orderId}/accept?courierId=${user.id}`,
                {},
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            alert("Заказ принят!");
            loadWaitingOrders();
        } catch (error) {
            alert("Ошибка принятия заказа");
        }
    };

    // ---- Стили ----
    const containerStyle = {
        minHeight: "100vh",
        background: "linear-gradient(135deg, #56ab2f 0%, #a8e063 100%)",
        padding: "40px 20px",
    };

    const cardStyle = {
        background: "white",
        borderRadius: "20px",
        padding: "40px",
        maxWidth: "900px",
        margin: "0 auto",
        boxShadow: "0 20px 60px rgba(0, 0, 0, 0.3)",
    };

    const headerStyle = {
        fontSize: "32px",
        color: "#333",
        marginTop: 0,
        marginBottom: "8px",
        fontWeight: "700",
    };

    const infoStyle = {
        fontSize: "16px",
        color: "#777",
        marginBottom: "10px",
    };

    const sectionTitleStyle = {
        fontSize: "24px",
        color: "#333",
        marginTop: "30px",
        marginBottom: "20px",
        fontWeight: "600",
        paddingBottom: "10px",
        borderBottom: "2px solid #e0e0e0",
    };

    const orderItemStyle = {
        background: "#f8f9fa",
        padding: "20px",
        borderRadius: "12px",
        marginBottom: "15px",
        border: "2px solid #e0e0e0",
    };

    const buttonStyle = {
        padding: "12px 24px",
        fontSize: "16px",
        fontWeight: "600",
        color: "white",
        background: "linear-gradient(135deg, #56ab2f 0%, #a8e063 100%)",
        border: "none",
        borderRadius: "8px",
        cursor: "pointer",
        marginTop: "12px",
    };

    const emptyStateStyle = {
        textAlign: "center",
        padding: "40px",
        color: "#999",
        fontSize: "16px",
    };

    return (
        <div style={containerStyle}>
            <div style={{ display: "flex", justifyContent: "flex-end", marginBottom: 20 }}>
                <button
                    onClick={() => setIsActive(!isActive)}
                    style={{
                        padding: "12px 24px",
                        fontSize: "16px",
                        fontWeight: "600",
                        background: isActive ? "#4CAF50" : "#d9534f",
                        color: "white",
                        border: "none",
                        borderRadius: "10px",
                        cursor: "pointer",
                    }}
                >
                    {isActive ? "Принимать заказы" : "Не принимать"}
                </button>
            </div>

            <div style={cardStyle}>
                <h2 style={headerStyle}>Курьер: {user.name}</h2>
                <p style={infoStyle}>Телефон: {user.phoneNumber}</p>
                <p style={infoStyle}>Транспорт: {user.transportType}</p>

                <h3 style={sectionTitleStyle}>Заказы, ожидающие курьера</h3>

                {!isActive ? (
                    <div style={emptyStateStyle}>Режим приёма заказов выключен</div>
                ) : waitingOrders.length === 0 ? (
                    <div style={emptyStateStyle}>Нет доступных заказов</div>
                ) : (
                    waitingOrders.map((order) => (
                        <div key={order.id} style={orderItemStyle}>
                            <strong style={{ fontSize: "18px", color: "#333" }}>
                                Заказ #{order.id}
                            </strong>
                            <p>
                                Точка A: ({order.pointALatitude}, {order.pointALongitude})
                            </p>
                            <p>
                                Точка B: ({order.pointBLatitude}, {order.pointBLongitude})
                            </p>

                            <button
                                onClick={() => acceptOrder(order.id)}
                                style={buttonStyle}
                            >
                                Принять заказ
                            </button>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
}
