package store.domain;

import static store.exception.ErrorMessage.INVALID_ITEM;
import static store.exception.ErrorMessage.INVALID_QUANTITY;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import store.dto.response.PromotionResponseDto;
import store.exception.StoreException;

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
            return new PromotionResponseDto(PromotionResult.NONE, purchaseCount, 0, 0, 0);
        }
        Item findItem = itemOptional.get();
        return findItem.getPromotionResult(purchaseCount, orderDate);
    }

    public void validatePurchase(String itemName, int count) {
        Validator.validateItemName(items, itemName);
        Validator.validateItemCount(items, itemName, count);
    }

    private static class Validator {
        public static void validateItemName(List<Item> items, String itemName) {
            boolean hasItemName = items.stream().anyMatch(item -> item.getName().equals(itemName));
            if (!hasItemName) {
                throw StoreException.from(INVALID_ITEM);
            }
        }

        public static void validateItemCount(List<Item> items, String itemName, int count) {
            int sum = items.stream().filter(item -> item.getName().equals(itemName))
                    .mapToInt(Item::getQuantity).sum();
            if (sum < count) {
                throw StoreException.from(INVALID_QUANTITY);
            }
        }
    }

}
