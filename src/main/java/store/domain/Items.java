package store.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import store.dto.response.PromotionResponseDto;

public class Items {
    private final List<Item> items;

    public Items(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void purchaseItem(String itemName, int quantity) {
        int count = quantity;
        List<Item> items = this.items.stream().filter(item -> item.getName().equals(itemName)).sorted()
                .toList();
        for (Item item : items) {
            count = item.tryPurchaseItem(count);
        }
    }

    public int getItemPrice(String itemName) {
        Optional<Item> findItem = items.stream().filter(item -> item.getName().equals(itemName)).findAny();
        return findItem.map(Item::getPrice).orElse(0);
    }

    public PromotionResponseDto getPromotionResult(String itemName, int purchaseCount, LocalDateTime orderDate) {
        Optional<Item> itemOptional = items.stream()
                .filter(item -> item.getName().equals(itemName) && item.hasPromotionEvent())
                .findAny();
        if (itemOptional.isEmpty()) {
            return PromotionResponseDto.of(PromotionResult.NONE, purchaseCount, 0, 0, 0);
        }
        Item findItem = itemOptional.get();
        if (!findItem.canApplyPromotion(orderDate)) {
            return PromotionResponseDto.of(PromotionResult.NONE, purchaseCount, 0, 0, 0);
        }
        return findItem.getPromotionResult(purchaseCount);
    }

    public void validatePurchase(String itemName, int count) {
        validateItemName(itemName);
        validateItemCount(itemName, count);
    }

    private void validateItemName(String itemName) {
        boolean hasItemName = items.stream().anyMatch(item -> item.getName().equals(itemName));
        if (!hasItemName) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        }
    }

    private void validateItemCount(String itemName, int count) {
        int sum = items.stream().filter(item -> item.getName().equals(itemName))
                .mapToInt(Item::getQuantity).sum();
        if (sum < count) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }
}
