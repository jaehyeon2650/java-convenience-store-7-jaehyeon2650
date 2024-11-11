package store.domain;

import java.time.LocalDateTime;
import java.util.Optional;
import store.dto.response.PromotionResponseDto;
import store.exception.ErrorMessage;
import store.exception.StoreException;

public class Item implements Comparable<Item> {
    private final String name;
    private final int price;
    private int quantity;
    private final Promotion promotion;

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
        if (promotion == null || !promotion.canApplyPromotion(orderDate)) {
            return new PromotionResponseDto(PromotionResult.NONE, purchaseCount, 0, 0, 0);
        }
        return promotion.getPromotionResult(quantity, purchaseCount);
    }

    public void purchaseItem(int quantity) {
        this.quantity -= quantity;
        if (this.quantity < 0) {
            throw StoreException.from(ErrorMessage.INVALID_QUANTITY);
        }
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
