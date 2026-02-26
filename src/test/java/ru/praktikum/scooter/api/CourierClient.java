package ru.praktikum.scooter.api;

import io.restassured.response.Response;
import ru.praktikum.scooter.data.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient {
    public static void deleteCourier(String login, String password) {
        // Сначала логинимся, получаем id
        Response loginResponse = given()
                .baseUri(Endpoints.BASE_URL)
                .contentType("application/json")
                .body(new CourierCredentials(login, password))
                .when()
                .post(Endpoints.LOGIN_COURIER)
                .then()
                .extract().response();

        if (loginResponse.statusCode() == 200) {
            int courierId = loginResponse.path("id");
            // Удаляем курьера
            given()
                    .baseUri(Endpoints.BASE_URL)
                    .pathParam("id", courierId)
                    .when()
                    .delete(Endpoints.DELETE_COURIER)
                    .then()
                    .statusCode(200);
        }
    }
}
