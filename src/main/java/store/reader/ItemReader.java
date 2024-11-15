package store.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.domain.Item;
import store.domain.Promotion;
import store.util.Reader;

public class ItemReader {
    private static final String DELIMITER = ",";
    private static final int NAME_INDEX = 0;
    private static final int PRICE_INDEX = 1;
    private static final int QUANTITY_INDEX = 2;
    private static final int PROMOTION_NAME_INDEX = 3;

    public List<Item> readItems(String file, List<Promotion> promotions) throws IOException {
        List<String> lines = Reader.readFile(file);
        List<Item> items = new ArrayList<>();
        lines.forEach(line -> {
            items.add(parseItem(line, promotions));
        });
        return addRegularStock(items);
    }

    private Item parseItem(String line, List<Promotion> promotions) {
        String[] parts = line.split(DELIMITER);
        String name = parts[NAME_INDEX];
        int price = Integer.parseInt(parts[PRICE_INDEX]);
        int quantity = Integer.parseInt(parts[QUANTITY_INDEX]);
        String promotionsName = parts[PROMOTION_NAME_INDEX];
        Optional<Promotion> promotionOptional = promotions.stream()
                .filter(promotion -> promotion.getPromotionName().equals(promotionsName)).findAny();
        return new Item(name, price, quantity, promotionOptional.orElse(null));
    }

    private List<Item> addRegularStock(List<Item> items) {
        List<Item> totalItemList = new ArrayList<>();
        for (Item item : items) {
            totalItemList.add(item);
            if (item.hasPromotionEvent() && !hasRegularStock(items, item.getName())) {
                totalItemList.add(new Item(item.getName(), item.getPrice(), 0, null));
            }
        }
        return totalItemList;
    }

    private boolean hasRegularStock(List<Item> items, String itemName) {
        for (Item item : items) {
            if (item.getName().equals(itemName) && !item.hasPromotionEvent()) {
                return true;
            }
        }
        return false;
    }
}
