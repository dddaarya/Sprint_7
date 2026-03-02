package ru.praktikum.scooter.tests;

import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.praktikum.scooter.api.Endpoints;
import ru.praktikum.scooter.data.Order;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AllureJunit5.class)
@DisplayName("Тесты создания заказа")
class CreateOrderTest {

    @ParameterizedTest
    @DisplayName("Создание заказа с разными цветами")
    @CsvSource({
            "BLACK, 'Только чёрный'",           // Только BLACK
            "GREY, 'Только серый'",            // Только GREY
            "BLACK,GREY, 'Оба цвета'",         // Оба цвета
            ", 'Без цвета'"                    // Пустой color = без цвета
    })
    void createOrderWithColors(String color, String testName) {
        Order order = new Order();
        order.setFirstName("Test");
        order.setLastName("User");
        order.setAddress("Test Street 1");
        order.setMetroStation(1);
        order.setPhone("+79991234567");
        order.setRentTime(1);
        order.setDeliveryDate("2026-02-25");
        order.setComment("Color test: " + testName);

        if (color != null && !color.isEmpty()) {
            order.setColor(color);
        }

        Response response = given()
                .baseUri(Endpoints.BASE_URL)
                .contentType("application/json")
                .body(order)
                .when()
                .post(Endpoints.CREATE_ORDER)
                .then()
                .extract().response();

        assertEquals(201, response.statusCode(), "Ожидается 201 для " + testName);
        assertNotNull(response.path("track"), "Трек не получен для " + testName);
    }
}
