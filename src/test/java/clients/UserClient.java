package clients;

import config.RestAssuredConfig;
import io.restassured.response.Response;
import models.User;

import static io.restassured.RestAssured.given;

public class UserClient {
    private static final String REGISTER_ENDPOINT = "/api/auth/register";
    private static final String LOGIN_ENDPOINT = "/api/auth/login";
    private static final String LOGOUT_ENDPOINT = "/api/auth/logout";
    private static final String USER_ENDPOINT = "/api/auth/user";

    public Response register(User user) {
        return given()
                .spec(RestAssuredConfig.getBaseSpec())
                .body(user)
                .post(REGISTER_ENDPOINT);
    }

    public Response login(User user) {
        return given()
                .spec(RestAssuredConfig.getBaseSpec())
                .body(user)
                .post(LOGIN_ENDPOINT);
    }

    public Response logout(String refreshToken) {
        return given()
                .spec(RestAssuredConfig.getBaseSpec())
                .body("{\"token\": \"" + refreshToken + "\"}")
                .post(LOGOUT_ENDPOINT);
    }

    public Response getUser(String accessToken) {
        return given()
                .spec(RestAssuredConfig.getBaseSpec())
                .header("Authorization", accessToken)
                .get(USER_ENDPOINT);
    }

    public String extractAccessToken(Response response) {
        String token = response.jsonPath().getString("accessToken");
        if (token != null && token.startsWith("Bearer ")) {
            return token;
        }
        return token;
    }

    public String extractRefreshToken(Response response) {
        return response.jsonPath().getString("refreshToken");
    }
}