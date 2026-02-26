package ru.praktikum.scooter.api;

public class Endpoints {
    public static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    public static final String CREATE_COURIER = BASE_URL + "/api/v1/courier";
    public static final String LOGIN_COURIER = BASE_URL + "/api/v1/courier/login";
    public static final String DELETE_COURIER = BASE_URL + "/api/v1/courier/{id}";

    public static final String CREATE_ORDER = BASE_URL + "/api/v1/orders";
    public static final String GET_ORDERS_LIST = BASE_URL + "/api/v1/orders";
}
