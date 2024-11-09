package store.dto.response;

import store.domain.Item;

public record ItemResponseDto(
        String name,
        int price,
        int quantity,
        String promotion
) {
    public static ItemResponseDto of(Item item) {
        return new ItemResponseDto(item.getName(), item.getPrice(), item.getQuantity(), item.getPromotionName());
    }
}
