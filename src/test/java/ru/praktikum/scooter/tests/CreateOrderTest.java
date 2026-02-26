package ru.praktikum.scooter.tests;

import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.praktikum.scooter.api.Endpoints;
import ru.praktikum.scooter.data.Order;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AllureJunit5.class)
@DisplayName("Тесты создания заказа")
class CreateOrderTest {

    @Test
    @DisplayName("Создание заказа БЕЗ цвета — по ТЗ")
    void createOrderWithoutColor() {
        Order order = new Order();
        order.setFirstName("Test");
        order.setLastName("User");
        order.setAddress("Test Street 1");
        order.setMetroStation(1);
        order.setPhone("+79991234567");
        order.setRentTime(1);
        order.setDeliveryDate("2026-02-25");
        order.setComment("No color test");

        Response response = given()
                .baseUri(Endpoints.BASE_URL)
                .contentType("application/json")
                .body(order)
                .when()
                .post(Endpoints.CREATE_ORDER)
                .then()
                .extract().response();

        assertEquals(201, response.statusCode());
        assertNotNull(response.path("track"));
    }
}
