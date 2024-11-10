package store.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.domain.Item;
import store.domain.Promotion;
import store.util.Reader;

public class ItemReader {
    public List<Item> readItems(String file, List<Promotion> promotions) throws IOException {
        List<String> lines = Reader.readFile(file);
        List<Item> items = new ArrayList<>();
        lines.forEach(line -> {
            items.add(parseItem(line, promotions));
        });
        return addRegularStock(items);
    }

    private Item parseItem(String line, List<Promotion> promotions) {
        String[] parts = line.split(",");
        String name = parts[0];
        int price = Integer.parseInt(parts[1]);
        int quantity = Integer.parseInt(parts[2]);
        String promotionsName = parts[3];
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
