import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

export default function UserHomePage({ user }) {
    const [orders, setOrders] = useState([]);
    const navigate = useNavigate();

    const token = localStorage.getItem("token");

    useEffect(() => {
        loadMyOrders();
        // eslint-disable-next-line react-hooks/exhaustive-deps
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

    const subHeaderStyle = {
        fontSize: "16px",
        color: "#777",
        marginBottom: "30px",
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

    const emptyStateStyle = {
        textAlign: "center",
        padding: "40px",
        color: "#999",
        fontSize: "16px",
    };

    const orderItemStyle = {
        background: "#f8f9fa",
        padding: "20px",
        borderRadius: "12px",
        marginBottom: "15px",
        border: "2px solid #e0e0e0",
    };

    return (
        <div style={containerStyle}>
            <div style={cardStyle}>
                <h2 style={headerStyle}>Добро пожаловать, {user.name}</h2>
                <p style={subHeaderStyle}>Телефон: {user.phoneNumber}</p>

                {/* Кнопка "Создать заказ" */}
                <div style={{ display: "flex", justifyContent: "flex-end" }}>
                    <button
                        onClick={() => navigate("/create-order")}
                        style={{
                            padding: "12px 24px",
                            background: "#56ab2f",
                            borderRadius: "10px",
                            color: "white",
                            border: "none",
                            cursor: "pointer",
                            marginBottom: "20px",
                            fontSize: "18px",
                        }}
                    >
                        Создать заказ
                    </button>
                </div>

                <h3 style={sectionTitleStyle}>Мои заказы</h3>

                {orders.length === 0 ? (
                    <div style={emptyStateStyle}>У вас пока нет заказов</div>
                ) : (
                    orders.map((o) => (
                        <div key={o.id} style={orderItemStyle}>
                            <strong style={{ fontSize: "18px" }}>Заказ #{o.id}</strong>
                            <p>
                                Статус:{" "}
                                <b style={{ color: "#56ab2f" }}>{o.orderStatus}</b>
                            </p>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
}
