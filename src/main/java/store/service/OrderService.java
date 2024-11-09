package store.service;

import store.domain.Items;

public class OrderService {

    private final Items items;

    public OrderService(Items items) {
        this.items = items;
    }
}
