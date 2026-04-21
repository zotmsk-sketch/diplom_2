package clients;

import config.RestAssuredConfig;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String ORDERS_ENDPOINT = "/api/orders";
    private static final String INGREDIENTS_ENDPOINT = "/api/ingredients";

    @Step("Создать заказ. Токен: {accessToken}")
    public Response createOrder(Order order, String accessToken) {
        var request = given().spec(RestAssuredConfig.getBaseSpec()).body(order);
        if (accessToken != null) {
            request.header("Authorization", accessToken);
        }
        return request.post(ORDERS_ENDPOINT);
    }

    @Step("Получить список ингредиентов")
    public Response getIngredients() {
        return given()
                .spec(RestAssuredConfig.getBaseSpec())
                .get(INGREDIENTS_ENDPOINT);
    }

    @Step("Получить валидный хеш ингредиента")
    public String getValidIngredientHash() {
        Response response = getIngredients();
        return response.jsonPath().getString("data[0]._id");
    }

    @Step("Получить невалидный хеш ингредиента")
    public String getInvalidIngredientHash() {
        return "invalid_hash_12345";
    }
}