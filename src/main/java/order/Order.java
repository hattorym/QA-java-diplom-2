package order;

import java.util.List;

public class Order {
    private List<String> ingredients;

    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public static Order getOrderCorrectHash() {
        return new Order(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));
    }

    public static Order getOrderNotCorrectHash() {
        return new Order(List.of("7777777777", "8888888888"));
    }

    public static Order getOrderEmptyList() {
        return new Order(List.of());
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

}
