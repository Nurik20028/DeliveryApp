import React, { useState, useRef } from "react";
import { MapContainer, TileLayer, Marker, useMapEvents } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import L from "leaflet";
import axios from "axios";
import { useNavigate } from "react-router-dom";

// FIX для маркеров (Leaflet баг в CRA)
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
    iconRetinaUrl:
        "https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png",
    iconUrl:
        "https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png",
    shadowUrl:
        "https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png",
});

export default function CreateOrderPage({ user }) {
    const [pointA, setPointA] = useState(null);
    const [pointB, setPointB] = useState(null);
    const [clickCount, setClickCount] = useState(0);
    const mapRef = useRef(null);
    const navigate = useNavigate();

    const token = localStorage.getItem("token");

    // КЛИКИ ПО КАРТЕ
    function MapClickHandler() {
        useMapEvents({
            click(e) {
                const { lat, lng } = e.latlng;

                setClickCount((prev) => {
                    if (prev === 0) {
                        setPointA({ lat, lng });
                    } else if (prev === 1) {
                        setPointB({ lat, lng });
                    }
                    return prev + 1;
                });
            },
        });
        return null;
    }

    const createOrder = async () => {
        if (!pointA || !pointB) {
            alert("Выберите точки A и B");
            return;
        }

        try {
            await axios.post(
                "http://localhost:8080/api/orders/create",
                null, // body не нужен, всё в params
                {
                    params: {
                        userId: user.id,
                        pointALat: pointA.lat,
                        pointALng: pointA.lng,
                        pointBLat: pointB.lat,
                        pointBLng: pointB.lng,
                    },
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            alert("Заказ создан!");

            // Сброс точек и клик-счётчика
            setPointA(null);
            setPointB(null);
            setClickCount(0);

            // Возврат на главную пользователя
            navigate("/home");
        } catch (e) {
            console.error(e);
            alert("Ошибка при создании заказа");
        }
    };

    return (
        <div style={{ padding: 20 }}>
            <h2>Создание заказа</h2>
            <p>Нажмите на карту, чтобы выбрать точки A и B (сначала A, потом B)</p>

            <MapContainer
                center={[42.8746, 74.5698]}
                zoom={13}
                style={{ height: "450px", width: "100%" }}
                ref={mapRef}
            >
                {/*<TileLayer url="https://tile.openstreetmap.fr/hot/{z}/{x}/{y}.png" />*/}
                <TileLayer
                    attribution='&copy; OpenStreetMap'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />

                <MapClickHandler />

                {pointA && <Marker position={[pointA.lat, pointA.lng]} />}
                {pointB && <Marker position={[pointB.lat, pointB.lng]} />}
            </MapContainer>

            <button
                onClick={createOrder}
                style={{
                    marginTop: 20,
                    padding: "12px 25px",
                    fontSize: "16px",
                    backgroundColor: "#56ab2f",
                    color: "white",
                    border: "none",
                    borderRadius: "8px",
                    cursor: "pointer",
                }}
            >
                Создать заказ
            </button>
        </div>
    );
}
