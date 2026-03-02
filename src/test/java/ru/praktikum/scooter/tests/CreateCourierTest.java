package ru.praktikum.scooter.tests;

import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.praktikum.scooter.steps.CourierSteps;  // ⭐ Используем Steps!

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AllureJunit5.class)
@DisplayName("Тесты создания курьера")
class CreateCourierTest {

    private String testLogin;
    private final CourierSteps courierSteps = new CourierSteps();  // ⭐ Steps вместо Client!

    @Test
    @DisplayName("Успешное создание курьера")
    void createCourierSuccess() {
        testLogin = "testlogin" + System.currentTimeMillis();

        courierSteps.createCourier(testLogin, "1234", "Test");

        assertTrue(courierSteps.getCreateResult().isSuccess());
    }

    @Test
    @DisplayName("Дубликат курьера — ошибка 409")
    void duplicateCourierError() {
        testLogin = "duptest" + System.currentTimeMillis();

        courierSteps.createCourier(testLogin, "1234", "Test");

        courierSteps.createCourier(testLogin, "1234", "Test");

        assertEquals(409, courierSteps.getCreateResult().getStatusCode());
    }

    @Test
    @DisplayName("Создание без логина — ошибка 400")
    void createWithoutLoginError() {
        courierSteps.createCourier(null, "1234", "Test");

        assertEquals(400, courierSteps.getCreateResult().getStatusCode());
    }

    @AfterEach
    void cleanupCourier() {
        if (testLogin != null) {
            System.out.println("🧹 Cleanup: удаляем курьера " + testLogin);

            try {
                courierSteps.deleteCourier(testLogin, "1234");
                System.out.println("🧹 Cleanup: курьер " + testLogin + " удалён");
            } catch (Exception e) {
                System.out.println("🧹 Cleanup: курьер " + testLogin + " уже удалён");
            }
        }
    }
}
