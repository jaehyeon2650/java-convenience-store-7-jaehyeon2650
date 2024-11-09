package store.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import store.dto.response.PromotionResponseDto;

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

    public boolean canApplyPromotion(LocalDate date) {
        return promotion.canApplyPromotion(date);
    }

    public PromotionResponseDto getPromotionResult(int purchaseCount) {
        return promotion.getPromotionResult(quantity, purchaseCount);
    }

}
