package ru.praktikum.scooter.tests;

import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.praktikum.scooter.api.Endpoints;
import ru.praktikum.scooter.data.Courier;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AllureJunit5.class)
@DisplayName("Тесты создания курьера")
class CreateCourierTest {

    private String testLogin;  // ⭐ ХРАНИМ ЛОГИН для @AfterEach

    @Test
    @DisplayName("Успешное создание курьера")
    void createCourierSuccess() {
        testLogin = "testlogin" + System.currentTimeMillis();
        Courier courier = new Courier(testLogin, "1234", "Test");

        Response response = given()
                .baseUri(Endpoints.BASE_URL)
                .contentType("application/json")
                .body(courier)
                .when()
                .post(Endpoints.CREATE_COURIER)
                .then()
                .extract().response();

        assertEquals(201, response.statusCode());
        assertEquals(true, response.path("ok"));
    }

    @Test
    @DisplayName("Дубликат курьера — ошибка 409")
    void duplicateCourierError() {
        testLogin = "duptest" + System.currentTimeMillis();

        // Создаём первого курьера
        given()
                .baseUri(Endpoints.BASE_URL)
                .contentType("application/json")
                .body(new Courier(testLogin, "1234", "Test"))
                .when()
                .post(Endpoints.CREATE_COURIER);

        // Пробуем создать дубликат
        Response response = given()
                .baseUri(Endpoints.BASE_URL)
                .contentType("application/json")
                .body(new Courier(testLogin, "1234", "Test"))
                .when()
                .post(Endpoints.CREATE_COURIER)
                .then()
                .extract().response();

        assertEquals(409, response.statusCode());
    }

    @Test
    @DisplayName("Создание без логина — ошибка 400")
    void createWithoutLoginError() {
        Courier courier = new Courier(null, "1234", "Test");

        Response response = given()
                .baseUri(Endpoints.BASE_URL)
                .contentType("application/json")
                .body(courier)
                .when()
                .post(Endpoints.CREATE_COURIER)
                .then()
                .extract().response();

        assertEquals(400, response.statusCode());
    }

    @AfterEach
    void cleanupCourier() {
        if (testLogin != null) {
            System.out.println("🧹 Cleanup: попытка удалить курьера " + testLogin);
            // Пока просто логируем - полное удаление позже
        }
    }
}
