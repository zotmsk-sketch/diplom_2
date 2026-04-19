package tests;

import clients.OrderClient;
import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import models.Order;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.UserGenerator;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;

public class CreateOrderTest {
    private OrderClient orderClient;
    private UserClient userClient;
    private User user;
    private String accessToken;
    private String validIngredientHash;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        userClient = new UserClient();

        user = UserGenerator.generateRandomUser();
        userClient.register(user).then().statusCode(200);
        accessToken = userClient.extractAccessToken(userClient.login(user).then().extract().response());
        validIngredientHash = orderClient.getValidIngredientHash();
    }

    @After
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            // можно добавить logout, но не обязательно
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Проверка успешного создания заказа авторизованным пользователем")
    public void createOrderWithAuthTest() {
        Order order = new Order(Collections.singletonList(validIngredientHash));
        var response = orderClient.createOrder(order, accessToken);

        response.then()
                .statusCode(200)
                .body("success", is(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка успешного создания заказа неавторизованным пользователем")
    public void createOrderWithoutAuthTest() {
        Order order = new Order(Collections.singletonList(validIngredientHash));
        var response = orderClient.createOrder(order, null);

        response.then()
                .statusCode(200)
                .body("success", is(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Проверка успешного создания заказа с корректными ингредиентами")
    public void createOrderWithIngredientsTest() {
        List<String> ingredients = List.of(validIngredientHash, orderClient.getValidIngredientHash());
        Order order = new Order(ingredients);
        var response = orderClient.createOrder(order, accessToken);

        response.then()
                .statusCode(200)
                .body("success", is(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка ошибки при создании заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        Order order = new Order(Collections.emptyList());
        var response = orderClient.createOrder(order, accessToken);

        response.then()
                .statusCode(400)
                .body("success", is(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Проверка ошибки при создании заказа с невалидным хешем ингредиента")
    public void createOrderWithInvalidIngredientHashTest() {
        Order order = new Order(Collections.singletonList(orderClient.getInvalidIngredientHash()));
        var response = orderClient.createOrder(order, accessToken);
        // Согласно документации API, невалидный хеш вызывает 500 Internal Server Error
        response.then()
                .statusCode(500);
    }
}