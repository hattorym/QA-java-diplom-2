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
import static user.UserRandomizer.faker;

public class OrderGetTest {

    private final UserSteps userSteps = new UserSteps();
    private final OrderSteps orderSteps = new OrderSteps();
    private Response response;
    private Order order;
    private String accessToken;

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userSteps.userDelete(accessToken);
        }
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getOrdersWithAuthorizedUserShouldReturnOk() {
        User user = UserRandomizer.createNewRandomUser();
        response = userSteps.userCreate(user);
        accessToken = response.then().extract().body().path("accessToken");
        order = Order.getOrderCorrectHash();
        response = orderSteps.createOrderWithToken(order, accessToken);
        response = orderSteps.getUserOrders(accessToken);
        response.then()
                .body("orders", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void getOrdersWithUnauthorizedUserShouldReturnError() {
        order = Order.getOrderCorrectHash();
        response = orderSteps.createOrderWithToken(order, String.valueOf(faker.random().hashCode()));
        response = orderSteps.getUserOrders(String.valueOf(faker.random().hashCode()));
        response.then()
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }
}