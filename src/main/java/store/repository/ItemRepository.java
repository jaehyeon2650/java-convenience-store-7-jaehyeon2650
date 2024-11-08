package store.repository;

import java.util.List;
import store.domain.Item;

public class ItemRepository {
    private final List<Item> items;

    public ItemRepository(List<Item> items) {
        this.items = items;
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
