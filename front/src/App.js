import React, { useState, useEffect } from "react";
import RegisterPage from "./pages/RegisterPage";
import LoginPage from "./pages/LoginPage";
import UserHomePage from "./pages/UserHomePage";
import CourierHomePage from "./pages/CourierHomePage";

export default function App() {
    const [page, setPage] = useState("register"); // register | login | home
    const [user, setUser] = useState(null);

    // Проверяем токен при загрузке
    useEffect(() => {
        const savedUser = localStorage.getItem("user");
        const token = localStorage.getItem("token");

        if (savedUser && token) {
            setUser(JSON.parse(savedUser));
            setPage("home");
        }
    }, []);

    // Когда успешно вошёл — сохраняем всё
    const handleLoginSuccess = (data) => {
        localStorage.setItem("token", data.accessToken);
        localStorage.setItem("user", JSON.stringify(data));

        setUser(data);
        setPage("home");
    };

    // Выход
    const logout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("user");

        setUser(null);
        setPage("register");
    };

    const logoutButtonStyle = {
        position: "absolute",
        top: "30px",
        right: "30px",
        padding: "12px 24px",
        fontSize: "16px",
        fontWeight: "600",
        color: "white",
        background: "rgba(255, 87, 87, 0.9)",
        backdropFilter: "blur(10px)",
        border: "2px solid rgba(255, 255, 255, 0.3)",
        borderRadius: "25px",
        cursor: "pointer",
        transition: "all 0.3s",
        zIndex: 1000,
        boxShadow: "0 4px 15px rgba(255, 87, 87, 0.4)"
    };

    return (
        <div>
            {/* ---------- REGISTER ---------- */}
            {page === "register" && (
                <RegisterPage onSwitchToLogin={() => setPage("login")} />
            )}

            {/* ---------- LOGIN ---------- */}
            {page === "login" && (
                <LoginPage
                    onSwitchToRegister={() => setPage("register")}
                    onLogin={handleLoginSuccess}
                />
            )}

            {/* ---------- HOME ---------- */}
            {page === "home" && user && (
                <div>
                    <button
                        style={logoutButtonStyle}
                        onClick={logout}
                        onMouseEnter={(e) => {
                            e.target.style.background = "rgba(255, 87, 87, 1)";
                            e.target.style.transform = "translateY(-2px)";
                            e.target.style.boxShadow = "0 6px 20px rgba(255, 87, 87, 0.6)";
                        }}
                        onMouseLeave={(e) => {
                            e.target.style.background = "rgba(255, 87, 87, 0.9)";
                            e.target.style.transform = "translateY(0)";
                            e.target.style.boxShadow = "0 4px 15px rgba(255, 87, 87, 0.4)";
                        }}
                    >
                        Выйти
                    </button>

                    {user.userType === "USER" && <UserHomePage user={user} />}
                    {user.userType === "COURIER" && <CourierHomePage user={user} />}
                </div>
            )}
        </div>
    );
}