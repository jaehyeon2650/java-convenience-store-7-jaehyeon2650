package store.domain;

public class Item {
    private String name;
    private int price;
    private int quantity;
    private Promotion promotion;

    public Item(String name, int price, int quantity, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }
}
