package store.domain;

import static store.exception.ErrorMessage.INVALID_ITEM;
import static store.exception.ErrorMessage.INVALID_QUANTITY;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
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
        validatePurchase(itemName, quantity);
        int totalCount = quantity;
        List<Item> list = items.stream().filter(item -> item.getName().equals(itemName)).sorted().toList();
        for (Item item : list) {
            int purchaseCount = Math.min(totalCount, item.getQuantity());
            item.purchaseItem(purchaseCount);
            totalCount -= purchaseCount;
        }
    }

    public int calculateItemsPrice(String itemName, int quantity) {
        Validator.validateItemName(items, itemName);
        Optional<Item> findItem = items.stream().filter(item -> item.getName().equals(itemName)).findAny();
        return findItem.map(item -> item.calculatePrice(quantity)).orElse(0);
    }

    public PromotionResponseDto getPromotionResult(String itemName, int purchaseCount, LocalDateTime orderDate) {
        Optional<Item> itemOptional = items.stream()
                .filter(item -> item.getName().equals(itemName))
                .sorted(Comparator.comparing(Item::hasPromotionEvent).reversed())
                .findFirst();
        if (itemOptional.isEmpty()) {
            throw StoreException.from(INVALID_ITEM);
        }
        return itemOptional.get().getPromotionResult(purchaseCount, orderDate);
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
