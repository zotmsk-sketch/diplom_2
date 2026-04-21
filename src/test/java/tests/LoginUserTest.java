package tests;

import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.UserGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class LoginUserTest {
    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.generateRandomUser();
        userClient.register(user).then().statusCode(SC_OK);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            // logout
        }
    }

    @Test
    @DisplayName("Вход под существующим пользователем")
    @Description("Проверка успешного входа зарегистрированного пользователя")
    public void loginExistingUserTest() {
        var response = userClient.login(user);
        response.then()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("user.email", equalTo(user.getEmail()));
        accessToken = userClient.extractAccessToken(response);
    }

    @Test
    @DisplayName("Вход с неверным email")
    @Description("Проверка ошибки при входе с неправильным email")
    public void loginWithWrongEmailTest() {
        User wrongUser = new User();
        wrongUser.setEmail("wrong@example.com");
        wrongUser.setPassword(user.getPassword());
        userClient.login(wrongUser).then()
                .statusCode(SC_UNAUTHORIZED)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Вход с неверным паролем")
    @Description("Проверка ошибки при входе с неправильным паролем")
    public void loginWithWrongPasswordTest() {
        User wrongUser = new User();
        wrongUser.setEmail(user.getEmail());
        wrongUser.setPassword("wrongpass");
        userClient.login(wrongUser).then()
                .statusCode(SC_UNAUTHORIZED)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Вход с неверными email и паролем")
    @Description("Проверка ошибки при входе с неправильными email и паролем")
    public void loginWithWrongEmailAndPasswordTest() {
        User wrongUser = new User();
        wrongUser.setEmail("wrong@example.com");
        wrongUser.setPassword("wrongpass");
        userClient.login(wrongUser).then()
                .statusCode(SC_UNAUTHORIZED)
                .body("message", equalTo("email or password are incorrect"));
    }
}