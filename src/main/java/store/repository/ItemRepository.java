package store.repository;

import java.util.List;
import store.domain.Item;

public class ItemRepository {
    private List<Item> items;

    public ItemRepository(List<Item> items) {
        this.items = items;
    }
}
