package order;

import java.util.List;

public class Order {
    private List<String> ingredients;

    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public static Order getOrderCorrectHash() {
        return new Order(List.of("60d3b41abdacab0026a733c6", "609646e4dc916e00276b2870"));
    }

    public static Order getOrderNotCorrectHash() {
        return new Order(List.of("7777777777", "8888888888"));
    }

    public static Order getOrderEmptyList() {
        return new Order(List.of());
    }

    public static Order getOrderMarsBurger() {
        return new Order(List.of("61c0c5a71d1f82001bdaaa71", "61c0c5a71d1f82001bdaaa6e"));
    }

}
