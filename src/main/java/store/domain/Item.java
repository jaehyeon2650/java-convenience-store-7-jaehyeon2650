package store.domain;

import java.time.LocalDateTime;
import java.util.Optional;
import store.dto.response.PromotionResponseDto;

public class Item implements Comparable<Item> {
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

    public boolean hasPromotionEvent() {
        return promotion != null;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int calculatePrice(int quantity) {
        return price * quantity;
    }

    public Optional<String> getPromotionName() {
        if (promotion == null) {
            return Optional.empty();
        }
        return Optional.of(promotion.getPromotionName());
    }

    public PromotionResponseDto getPromotionResult(int purchaseCount, LocalDateTime orderDate) {
        return promotion.getPromotionResult(quantity, purchaseCount, orderDate);
    }

    public int tryPurchaseItem(int quantity) {
        if (this.quantity < quantity) {
            int rest = (quantity - this.quantity);
            this.quantity = 0;
            return rest;
        }
        this.quantity -= quantity;
        return 0;
    }


    @Override
    public int compareTo(Item other) {
        if (this.promotion == null && other.promotion != null) {
            return 1;
        }
        if (this.promotion != null && other.promotion == null) {
            return -1;
        }
        return 0;
    }
}
