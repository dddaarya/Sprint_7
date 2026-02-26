package ru.praktikum.scooter.tests;

import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.praktikum.scooter.api.Endpoints;
import ru.praktikum.scooter.data.Courier;
import ru.praktikum.scooter.data.CourierCredentials;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AllureJunit5.class)
@DisplayName("Тесты логина курьера")
class LoginCourierTest {

    private String testLogin;

    @Test
    @DisplayName("Успешный логин курьера")
    void loginCourierSuccess() {
        testLogin = "testlogin" + System.currentTimeMillis();
        Courier courier = new Courier(testLogin, "1234", "Test");

        given().baseUri(Endpoints.BASE_URL).contentType("application/json").body(courier)
                .post(Endpoints.CREATE_COURIER);

        Response response = given()
                .baseUri(Endpoints.BASE_URL)
                .contentType("application/json")
                .body(new CourierCredentials(testLogin, "1234"))
                .when()
                .post(Endpoints.LOGIN_COURIER)
                .then()
                .extract().response();

        assertEquals(200, response.statusCode());
        assertNotNull(response.path("id"));
    }

    @Test
    @DisplayName("Неправильный пароль — ошибка 404")
    void wrongPasswordError() {
        testLogin = "wrongpass" + System.currentTimeMillis();
        given().baseUri(Endpoints.BASE_URL).contentType("application/json")
                .body(new Courier(testLogin, "1234", "Test")).post(Endpoints.CREATE_COURIER);

        Response response = given()
                .baseUri(Endpoints.BASE_URL)
                .contentType("application/json")
                .body(new CourierCredentials(testLogin, "WRONG"))
                .when()
                .post(Endpoints.LOGIN_COURIER)
                .then()
                .extract().response();

        assertEquals(404, response.statusCode());
    }

    @Test
    @DisplayName("Без логина — ошибка 400")
    void loginWithoutLoginError() {
        Response response = given()
                .baseUri(Endpoints.BASE_URL)
                .contentType("application/json")
                .body(new CourierCredentials(null, "1234"))
                .when()
                .post(Endpoints.LOGIN_COURIER)
                .then()
                .extract().response();

        assertEquals(400, response.statusCode());
    }

    @AfterEach
    void cleanupCourier() {
        if (testLogin != null) {
            System.out.println("🧹 Cleanup: " + testLogin);
        }
    }
}
