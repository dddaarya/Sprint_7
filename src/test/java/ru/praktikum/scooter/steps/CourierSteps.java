package ru.praktikum.scooter.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.scooter.api.Endpoints;
import ru.praktikum.scooter.data.Courier;
import ru.praktikum.scooter.data.CourierCredentials;
import ru.praktikum.scooter.client.CourierClient;

import static io.restassured.RestAssured.given;

public class CourierSteps {
    private final CourierClient courierClient = new CourierClient();
    private Response lastResponse;

    @Step("Создание курьера: login={0}, password={1}")
    public void createCourier(String login, String password, String firstName) {
        Courier courier = new Courier(login, password, firstName);
        lastResponse = given()
                .baseUri(Endpoints.BASE_URL)
                .contentType("application/json")
                .body(courier)
                .when()
                .post(Endpoints.CREATE_COURIER);
    }

    @Step("Логин курьера: login={0}")
    public void loginCourier(String login, String password) {
        CourierCredentials credentials = new CourierCredentials(login, password);
        lastResponse = given()
                .baseUri(Endpoints.BASE_URL)
                .contentType("application/json")
                .body(credentials)
                .when()
                .post(Endpoints.LOGIN_COURIER);
    }

    @Step("Удаление курьера: login={0}")
    public void deleteCourier(String login, String password) {
        loginCourier(login, password);  // Получаем ID
        courierClient.deleteCourier(login, password);  // Удаляем
    }

    public Response getLastResponse() {
        return lastResponse;
    }

    public boolean isSuccess() {
        return lastResponse != null && lastResponse.path("ok") == Boolean.TRUE;
    }

    public int getStatusCode() {
        return lastResponse != null ? lastResponse.statusCode() : 0;
    }

    public CreateResult getCreateResult() {
        return new CreateResult(lastResponse.statusCode(), isSuccess());
    }

    public static class CreateResult {
        private final int statusCode;
        private final boolean success;

        public CreateResult(int statusCode, boolean success) {
            this.statusCode = statusCode;
            this.success =
