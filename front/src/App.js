import React, { useState, useEffect } from "react";
import {
    BrowserRouter as Router,
    Routes,
    Route,
    useNavigate,
} from "react-router-dom";

import RegisterPage from "./pages/RegisterPage";
import LoginPage from "./pages/LoginPage";
import UserHomePage from "./pages/UserHomePage";
import CourierHomePage from "./pages/CourierHomePage";
import CreateOrderPage from "./pages/CreateOrderPage";

export default function App() {
    return (
        <Router>
            <MainApp />
        </Router>
    );
}

// function MainApp() {
//     const [user, setUser] = useState(null);
//     const navigate = useNavigate();
//
//     // При первой загрузке пробуем восстановить юзера
//     useEffect(() => {
//         const savedUser = localStorage.getItem("user");
//         const token = localStorage.getItem("token");
//
//         if (savedUser && token) {
//             setUser(JSON.parse(savedUser));
//             navigate("/home");
//         } else {
//             navigate("/register");
//         }
//     }, [navigate]);
function MainApp() {
    const [user, setUser] = useState(null);
    const [initialized, setInitialized] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const savedUser = localStorage.getItem("user");
        const token = localStorage.getItem("token");

        if (savedUser && token) {
            setUser(JSON.parse(savedUser));
            navigate("/home");
        }

        setInitialized(true);
    }, []); // ← важное изменение: пустой массив

    if (!initialized) {
        return null; // или загрузочный экран
    }


    const handleLoginSuccess = (data) => {
        alert("Токен: " + data.accessToken);
        localStorage.setItem("token", data.accessToken);
        localStorage.setItem("user", JSON.stringify(data));

        setUser(data);
        navigate("/home");
    };

    const logout = () => {
        localStorage.clear();
        setUser(null);
        navigate("/login");
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
        border: "none",
        borderRadius: "25px",
        cursor: "pointer",
        boxShadow: "0 4px 15px rgba(255, 87, 87, 0.4)",
        zIndex: 1000,
    };

    return (
        <div>
            {user && (
                <button
                    style={logoutButtonStyle}
                    onClick={logout}
                    onMouseEnter={(e) => {
                        e.target.style.background = "rgba(255, 87, 87, 1)";
                        e.target.style.transform = "translateY(-2px)";
                    }}
                    onMouseLeave={(e) => {
                        e.target.style.background = "rgba(255, 87, 87, 0.9)";
                        e.target.style.transform = "translateY(0)";
                    }}
                >
                    Выйти
                </button>
            )}

            <Routes>
                <Route
                    path="/register"
                    element={
                        <RegisterPage onSwitchToLogin={() => navigate("/login")} />
                    }
                />

                <Route
                    path="/login"
                    element={
                        <LoginPage
                            onSwitchToRegister={() => navigate("/register")}
                            onLogin={handleLoginSuccess}
                        />
                    }
                />

                <Route
                    path="/home"
                    element={
                        user ? (
                            user.userType === "USER" ? (
                                <UserHomePage user={user} />
                            ) : (
                                <CourierHomePage user={user} />
                            )
                        ) : (
                            <LoginPage
                                onSwitchToRegister={() => navigate("/register")}
                                onLogin={handleLoginSuccess}
                            />
                        )
                    }
                />

                <Route
                    path="/create-order"
                    element={
                        user ? (
                            <CreateOrderPage user={user} />
                        ) : (
                            <LoginPage
                                onSwitchToRegister={() => navigate("/register")}
                                onLogin={handleLoginSuccess}
                            />
                        )
                    }
                />

                {/* редирект по умолчанию */}
                <Route
                    path="*"
                    element={
                        <RegisterPage onSwitchToLogin={() => navigate("/login")} />
                    }
                />
            </Routes>
        </div>
    );
}
