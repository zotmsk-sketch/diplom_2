package tests;

import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.UserGenerator;

import static org.hamcrest.Matchers.*;

public class CreateUserTest {
    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            // можно добавить logout
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUniqueUserTest() {
        user = UserGenerator.generateRandomUser();
        var response = userClient.register(user);
        response.then()
                .statusCode(200)
                .body("success", is(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", notNullValue());
        accessToken = userClient.extractAccessToken(response);
    }

    @Test
    @DisplayName("Создание уже существующего пользователя")
    public void createExistingUserTest() {
        user = UserGenerator.generateRandomUser();
        userClient.register(user).then().statusCode(200);
        var secondResponse = userClient.register(user);
        secondResponse.then()
                .statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание без email")
    public void createUserWithoutEmailTest() {
        user = UserGenerator.generateUserWithMissingEmail();
        var response = userClient.register(user);
        response.then()
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание без пароля")
    public void createUserWithoutPasswordTest() {
        user = UserGenerator.generateUserWithMissingPassword();
        var response = userClient.register(user);
        response.then()
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание без имени")
    public void createUserWithoutNameTest() {
        user = UserGenerator.generateUserWithMissingName();
        var response = userClient.register(user);
        response.then()
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
    }
}