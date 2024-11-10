package store.dto.response;

import java.util.Optional;
import store.domain.Item;

public record ItemResponseDto(
        String name,
        int price,
        int quantity,
        Optional<String> promotion
) {
    public ItemResponseDto(Item item) {
        this(item.getName(), item.getPrice(), item.getQuantity(), item.getPromotionName());
    }
}
