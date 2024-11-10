package store;

import store.config.StoreConfig;
import store.controller.OrderController;

public class Application {
    public static void main(String[] args) {
        StoreConfig storeConfig = new StoreConfig();
        OrderController orderController = storeConfig.orderController();
        orderController.run();
    }
}
