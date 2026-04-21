package clients;

import config.RestAssuredConfig;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.User;

import static io.restassured.RestAssured.given;

public class UserClient {
    private static final String REGISTER_ENDPOINT = "/api/auth/register";
    private static final String LOGIN_ENDPOINT = "/api/auth/login";
    private static final String LOGOUT_ENDPOINT = "/api/auth/logout";
    private static final String USER_ENDPOINT = "/api/auth/user";

    @Step("Регистрация пользователя: {user.email}")
    public Response register(User user) {
        return given()
                .spec(RestAssuredConfig.getBaseSpec())
                .body(user)
                .post(REGISTER_ENDPOINT);
    }

    @Step("Логин пользователя: {user.email}")
    public Response login(User user) {
        return given()
                .spec(RestAssuredConfig.getBaseSpec())
                .body(user)
                .post(LOGIN_ENDPOINT);
    }

    @Step("Выход из системы, refreshToken: {refreshToken}")
    public Response logout(String refreshToken) {
        return given()
                .spec(RestAssuredConfig.getBaseSpec())
                .body("{\"token\": \"" + refreshToken + "\"}")
                .post(LOGOUT_ENDPOINT);
    }

    @Step("Получить данные пользователя по токену")
    public Response getUser(String accessToken) {
        return given()
                .spec(RestAssuredConfig.getBaseSpec())
                .header("Authorization", accessToken)
                .get(USER_ENDPOINT);
    }

    @Step("Извлечь accessToken из ответа")
    public String extractAccessToken(Response response) {
        String token = response.jsonPath().getString("accessToken");
        if (token != null && token.startsWith("Bearer ")) {
            return token;
        }
        return token;
    }

    @Step("Извлечь refreshToken из ответа")
    public String extractRefreshToken(Response response) {
        return response.jsonPath().getString("refreshToken");
    }
}