package ru.praktikum.scooter.tests;

import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.praktikum.scooter.api.Endpoints;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AllureJunit5.class)
@DisplayName("Тесты списка заказов")
public class GetOrdersTest {

    @Test
    @DisplayName("Получение списка заказов")
    public void getOrdersListSuccess() {
        Response response = given()
                .baseUri(Endpoints.BASE_URL)
                .when()
                .get(Endpoints.GET_ORDERS_LIST)
                .then()
                .extract().response();

        assertEquals(200, response.statusCode());
        assertNotNull(response.path("orders"));
        assertTrue(response.path("orders") instanceof java.util.List);
    }
}
