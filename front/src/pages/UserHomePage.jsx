import React, { useEffect, useState } from "react";
import axios from "axios";

export default function UserHomePage({ user }) {
    const [orders, setOrders] = useState([]);

    const token = localStorage.getItem("token");

    useEffect(() => {
        loadMyOrders();
    }, []);

    const loadMyOrders = async () => {
        try {
            const response = await axios.get(
                `http://localhost:8080/api/orders/user/${user.id}`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            setOrders(response.data);
        } catch (error) {
            console.log("Ошибка загрузки заказов", error);
        }
    };

    const containerStyle = {
        minHeight: "100vh",
        background: "linear-gradient(135deg, #56ab2f 0%, #a8e063 100%)",
        padding: "40px 20px"
    };

    const cardStyle = {
        background: "white",
        borderRadius: "20px",
        padding: "40px",
        maxWidth: "900px",
        margin: "0 auto",
        boxShadow: "0 20px 60px rgba(0, 0, 0, 0.3)"
    };

    const headerStyle = {
        fontSize: "32px",
        color: "#333",
        marginTop: 0,
        marginBottom: "8px",
        fontWeight: "700"
    };

    const subHeaderStyle = {
        fontSize: "16px",
        color: "#777",
        marginBottom: "30px"
    };

    const sectionTitleStyle = {
        fontSize: "24px",
        color: "#333",
        marginTop: "30px",
        marginBottom: "20px",
        fontWeight: "600",
        paddingBottom: "10px",
        borderBottom: "2px solid #e0e0e0"
    };

    const orderItemStyle = {
        background: "#f8f9fa",
        padding: "20px",
        borderRadius: "12px",
        marginBottom: "15px",
        border: "2px solid #e0e0e0",
        transition: "all 0.3s"
    };

    const emptyStateStyle = {
        textAlign: "center",
        padding: "40px",
        color: "#999",
        fontSize: "16px"
    };

    return (
        <div style={containerStyle}>
            <div style={cardStyle}>
                <h2 style={headerStyle}>Добро пожаловать, {user.name}</h2>
                <p style={subHeaderStyle}>Телефон: {user.phoneNumber}</p>

                <h3 style={sectionTitleStyle}>Мои заказы</h3>

                {orders.length === 0 ? (
                    <div style={emptyStateStyle}>
                        <p>У вас пока нет заказов</p>
                    </div>
                ) : (
                    <div>
                        {orders.map((o) => (
                            <div
                                key={o.id}
                                style={orderItemStyle}
                                onMouseEnter={(e) => {
                                    e.currentTarget.style.borderColor = "#56ab2f";
                                    e.currentTarget.style.transform = "translateY(-2px)";
                                }}
                                onMouseLeave={(e) => {
                                    e.currentTarget.style.borderColor = "#e0e0e0";
                                    e.currentTarget.style.transform = "translateY(0)";
                                }}
                            >
                                <strong style={{ fontSize: "18px", color: "#333" }}>
                                    Заказ #{o.id}
                                </strong>
                                <p style={{ margin: "8px 0 0 0", color: "#666" }}>
                                    Статус: <span style={{
                                    color: "#56ab2f",
                                    fontWeight: "600"
                                }}>{o.orderStatus}</span>
                                </p>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}