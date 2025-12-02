import React, { useState } from "react";
import { Formik, Form, Field } from "formik";
import * as Yup from "yup";
import axios from "axios";

export default function RegisterPage({ onSwitchToLogin }) {
    const [userType, setUserType] = useState("USER");

    const schema = Yup.object().shape({
        name: Yup.string().required("Введите имя"),
        phoneNumber: Yup.string().required("Введите номер телефона"),
        password: Yup.string().required("Введите пароль"),
        userType: Yup.string().required(),

        transportType: Yup.string().when("userType", (userType, schema) => {
            return userType === "COURIER"
                ? schema.required("Выберите тип транспорта")
                : schema;
        }),

        transportNumber: Yup.string().when("userType", (userType, schema) => {
            return userType === "COURIER"
                ? schema.required("Введите номер транспорта")
                : schema;
        }),
    });


    const register = async (values, { resetForm }) => {
        try {
            const payload = {
                name: values.name,
                phoneNumber: values.phoneNumber,
                password: values.password,
                userType: values.userType,
                transportType: values.userType === "COURIER" ? values.transportType : null,
                transportNumber: values.userType === "COURIER" ? values.transportNumber : null,
            };

            console.log("👉 payload:", payload);

            await axios.post("http://localhost:8080/api/auth/register", payload);

            alert("Регистрация успешна!");
            resetForm();
            onSwitchToLogin();
        } catch (error) {
            console.log("❌ Ошибка регистрации:", error);
            alert(error.response?.data || "Ошибка регистрации");
        }
    };



    const inputStyle = {
        width: "100%",
        padding: "14px 18px",
        fontSize: "16px",
        border: "2px solid #e0e0e0",
        borderRadius: "10px",
        outline: "none",
        transition: "all 0.3s",
        boxSizing: "border-box",
        marginBottom: "8px"
    };

    const labelStyle = {
        display: "block",
        marginBottom: "8px",
        marginTop: "16px",
        color: "#555",
        fontSize: "14px",
        fontWeight: "600"
    };

    const errorStyle = {
        color: "#e74c3c",
        fontSize: "13px",
        marginTop: "4px",
        marginBottom: "8px"
    };

    const buttonStyle = {
        width: "100%",
        padding: "16px",
        fontSize: "18px",
        fontWeight: "600",
        color: "white",
        background: "linear-gradient(135deg, #56ab2f 0%, #a8e063 100%)",
        border: "none",
        borderRadius: "10px",
        cursor: "pointer",
        transition: "transform 0.2s, box-shadow 0.2s",
        boxShadow: "0 4px 15px rgba(86, 171, 47, 0.4)",
        marginTop: "20px"
    };

    const switchButtonStyle = {
        position: "absolute",
        top: "30px",
        left: "30px",
        padding: "12px 24px",
        fontSize: "16px",
        fontWeight: "600",
        color: "white",
        background: "rgba(255, 255, 255, 0.2)",
        backdropFilter: "blur(10px)",
        border: "2px solid rgba(255, 255, 255, 0.3)",
        borderRadius: "25px",
        cursor: "pointer",
        transition: "all 0.3s",
        zIndex: 10
    };

    const secondaryButtonStyle = {
        width: "100%",
        padding: "12px",
        fontSize: "16px",
        fontWeight: "500",
        color: "#56ab2f",
        background: "white",
        border: "2px solid #56ab2f",
        borderRadius: "10px",
        cursor: "pointer",
        transition: "all 0.3s",
        marginTop: "12px"
    };

    return (
        <div style={{
            minHeight: "100vh",
            background: "linear-gradient(135deg, #56ab2f 0%, #a8e063 100%)",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            padding: "20px",
            position: "relative"
        }}>
            <button
                onClick={onSwitchToLogin}
                style={switchButtonStyle}
                onMouseEnter={(e) => {
                    e.target.style.background = "rgba(255, 255, 255, 0.3)";
                    e.target.style.transform = "translateY(-2px)";
                }}
                onMouseLeave={(e) => {
                    e.target.style.background = "rgba(255, 255, 255, 0.2)";
                    e.target.style.transform = "translateY(0)";
                }}
            >
                Вход →
            </button>

            <div style={{
                background: "white",
                borderRadius: "20px",
                padding: "40px",
                boxShadow: "0 20px 60px rgba(0, 0, 0, 0.3)",
                maxWidth: "450px",
                width: "100%"
            }}>
                <h2 style={{
                    fontSize: "32px",
                    marginBottom: "10px",
                    marginTop: 0,
                    color: "#333",
                    textAlign: "center",
                    fontWeight: "700"
                }}>
                    Регистрация
                </h2>

                <Formik
                    initialValues={{
                        name: "",
                        phoneNumber: "",
                        password: "",
                        userType: "USER",
                        transportType: "",
                        transportNumber: "",
                    }}
                    validationSchema={schema}
                    onSubmit={register}
                >
                    {({ errors, touched, values, setFieldValue }) => (
                        <Form>
                            <label style={labelStyle}>Имя</label>
                            <Field
                                name="name"
                                style={inputStyle}
                                onFocus={(e) => e.target.style.borderColor = "#56ab2f"}
                                onBlur={(e) => e.target.style.borderColor = "#e0e0e0"}
                            />
                            {errors.name && touched.name && (
                                <div style={errorStyle}>{errors.name}</div>
                            )}

                            <label style={labelStyle}>Телефон</label>
                            <Field
                                name="phoneNumber"
                                style={inputStyle}
                                onFocus={(e) => e.target.style.borderColor = "#56ab2f"}
                                onBlur={(e) => e.target.style.borderColor = "#e0e0e0"}
                            />
                            {errors.phoneNumber && touched.phoneNumber && (
                                <div style={errorStyle}>{errors.phoneNumber}</div>
                            )}

                            <label style={labelStyle}>Пароль</label>
                            <Field
                                name="password"
                                type="password"
                                style={inputStyle}
                                onFocus={(e) => e.target.style.borderColor = "#56ab2f"}
                                onBlur={(e) => e.target.style.borderColor = "#e0e0e0"}
                            />
                            {errors.password && touched.password && (
                                <div style={errorStyle}>{errors.password}</div>
                            )}

                            <label style={labelStyle}>Тип пользователя</label>
                            <Field
                                as="select"
                                name="userType"
                                style={{...inputStyle, cursor: "pointer"}}
                                onChange={(e) => {
                                    setUserType(e.target.value);
                                    setFieldValue("userType", e.target.value);
                                }}
                                onFocus={(e) => e.target.style.borderColor = "#56ab2f"}
                                onBlur={(e) => e.target.style.borderColor = "#e0e0e0"}
                            >
                                <option value="USER">Пользователь</option>
                                <option value="COURIER">Курьер</option>
                            </Field>

                            {userType === "COURIER" && (
                                <>
                                    <label style={labelStyle}>Тип транспорта</label>
                                    <Field
                                        as="select"
                                        name="transportType"
                                        style={{...inputStyle, cursor: "pointer"}}
                                        onFocus={(e) => e.target.style.borderColor = "#56ab2f"}
                                        onBlur={(e) => e.target.style.borderColor = "#e0e0e0"}
                                    >
                                        <option value="">Выберите</option>
                                        <option value="BIKE">Мопед</option>
                                        <option value="CAR">Машина</option>
                                        <option value="TRUCK">Грузовик</option>
                                    </Field>


                                    {errors.transportType &&
                                        touched.transportType && (
                                            <div style={errorStyle}>
                                                {errors.transportType}
                                            </div>
                                        )}

                                    <label style={labelStyle}>Номер транспорта</label>
                                    <Field
                                        name="transportNumber"
                                        style={inputStyle}
                                        onFocus={(e) => e.target.style.borderColor = "#56ab2f"}
                                        onBlur={(e) => e.target.style.borderColor = "#e0e0e0"}
                                    />
                                    {errors.transportNumber &&
                                        touched.transportNumber && (
                                            <div style={errorStyle}>
                                                {errors.transportNumber}
                                            </div>
                                        )}
                                </>
                            )}

                            <button
                                type="submit"
                                style={buttonStyle}
                                onMouseEnter={(e) => {
                                    e.target.style.transform = "translateY(-2px)";
                                    e.target.style.boxShadow = "0 6px 20px rgba(86, 171, 47, 0.6)";
                                }}
                                onMouseLeave={(e) => {
                                    e.target.style.transform = "translateY(0)";
                                    e.target.style.boxShadow = "0 4px 15px rgba(86, 171, 47, 0.4)";
                                }}
                            >
                                Зарегистрироваться
                            </button>

                            <button
                                type="button"
                                style={secondaryButtonStyle}
                                onClick={onSwitchToLogin}
                                onMouseEnter={(e) => {
                                    e.target.style.background = "#f0f9f0";
                                }}
                                onMouseLeave={(e) => {
                                    e.target.style.background = "white";
                                }}
                            >
                                Уже есть аккаунт? Войти
                            </button>
                        </Form>
                    )}
                </Formik>
            </div>
        </div>
    );
}