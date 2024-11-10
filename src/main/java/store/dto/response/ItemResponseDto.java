package store.dto.response;

import store.domain.Item;

public record ItemResponseDto(
        String name,
        int price,
        int quantity,
        String promotion
) {
    public ItemResponseDto(Item item) {
        this(item.getName(), item.getPrice(), item.getQuantity(), item.getPromotionName());
    }
}
