import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.Order;
import order.OrderSteps;
import org.junit.After;
import org.junit.Test;
import user.User;
import user.UserRandomizer;
import user.UserSteps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderCreateTest {
    private final UserSteps userSteps = new UserSteps();
    private final OrderSteps orderSteps = new OrderSteps();
    private String accessToken;

    private Response response;

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userSteps.userDelete(accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией пользователя и с валидным хэшем ингредиентов")
    public void createOrderWithUserLoginAndCorrectIngHashShouldReturnOk() {
        User user = UserRandomizer.createNewRandomUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response = orderSteps.createOrderWithToken(Order.getOrderCorrectHash(), accessToken);
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Создание заказа без авторизации пользователя и с пустым хэшем ингредиентов")
    public void createOrderWithoutUserLoginAndEmptyIngHashShouldReturnOk() {
        response = orderSteps.createOrderWithToken(Order.getOrderCorrectHash(), "");
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Создание заказа без хэша ингредиентов")
    public void createOrderWithoutUserLoginAndWithoutIngHashShouldReturnOk() {
        response = orderSteps.createOrderWithoutToken(Order.getOrderCorrectHash());
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Создание заказа с пустым списком ингредиентов")
    public void createOrderWithoutIngredientsShouldReturnError() {
        response = orderSteps.createOrderWithoutToken(Order.getOrderEmptyList());
        response.then()
                .body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithWrongIngredientHashReturnError() {
        Order order = Order.getOrderNotCorrectHash();
        response = orderSteps.createOrderWithoutToken(order);
        response.then().
                statusCode(500);
    }

    @Test
    @DisplayName("Проверка наличия ингредиентов в базе")
    public void getStatusOfIngredients() {
        response = orderSteps.getIngredients();
        response.then()
                .body("success", equalTo(true))
                .and()
                .body("data", notNullValue())
                .and()
                .statusCode(200);
    }

}

