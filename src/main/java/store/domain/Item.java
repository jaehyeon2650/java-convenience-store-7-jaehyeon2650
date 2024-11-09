package store.domain;

import java.time.LocalDateTime;
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

    public String getName() {
        return name;
    }

    public boolean hasPromotionEvent() {
        return promotion != null;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPromotionName() {
        if (promotion == null) {
            return null;
        }
        return promotion.getName();
    }

    public boolean canApplyPromotion(LocalDateTime date) {
        return promotion.canApplyPromotion(date);
    }

    public PromotionResponseDto getPromotionResult(int purchaseCount) {
        return promotion.getPromotionResult(quantity, purchaseCount);
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
