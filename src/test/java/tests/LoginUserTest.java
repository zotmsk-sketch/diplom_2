package tests;

import clients.UserClient;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.UserGenerator;

import static org.hamcrest.Matchers.*;

public class LoginUserTest {
    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.generateRandomUser();
        userClient.register(user).then().statusCode(200);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            // logout
        }
    }

    @Test
    public void loginExistingUserTest() {
        var response = userClient.login(user);
        response.then()
                .statusCode(200)
                .body("success", is(true))
                .body("user.email", equalTo(user.getEmail()));
        accessToken = userClient.extractAccessToken(response);
    }

    @Test
    public void loginWithWrongEmailTest() {
        User wrongUser = new User();
        wrongUser.setEmail("wrong@example.com");
        wrongUser.setPassword(user.getPassword());
        userClient.login(wrongUser).then()
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void loginWithWrongPasswordTest() {
        User wrongUser = new User();
        wrongUser.setEmail(user.getEmail());
        wrongUser.setPassword("wrongpass");
        userClient.login(wrongUser).then()
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void loginWithWrongEmailAndPasswordTest() {
        User wrongUser = new User();
        wrongUser.setEmail("wrong@example.com");
        wrongUser.setPassword("wrongpass");
        userClient.login(wrongUser).then()
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }
}