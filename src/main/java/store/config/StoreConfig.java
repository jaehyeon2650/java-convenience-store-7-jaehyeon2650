package store.config;

import java.io.IOException;
import java.util.List;
import store.controller.OrderController;
import store.domain.Items;
import store.domain.Promotion;
import store.exception.ErrorMessage;
import store.exception.StoreException;
import store.reader.ItemReader;
import store.reader.PromotionReader;
import store.service.OrderService;
import store.view.InputView;
import store.view.OutputView;

public class StoreConfig {
    private static OrderService orderService;
    private static OutputView outputView;
    private static InputView inputView;
    private static OrderController orderController;

    public OrderService orderService() {
        if (orderService == null) {
            Items items = Initialize.initializeItems();
            orderService = new OrderService(items);
        }
        return orderService;
    }

    public OutputView outputView() {
        if (outputView == null) {
            outputView = new OutputView();
        }
        return outputView;
    }

    public InputView inputView() {
        if (inputView == null) {
            inputView = new InputView();
        }
        return inputView;
    }

    public OrderController orderController() {
        if (orderController == null) {
            orderController = new OrderController(outputView(), inputView(), orderService());
        }
        return orderController;
    }

    private static class Initialize {
        private static final String ITEMS_FILE = "src/main/resources/products.md";
        private static final String PROMOTION_FILE = "src/main/resources/promotions.md";

        public static Items initializeItems() throws StoreException {
            try {
                PromotionReader promotionReader = new PromotionReader();
                ItemReader itemReader = new ItemReader();
                List<Promotion> promotions = promotionReader.readPromotions(PROMOTION_FILE);
                return new Items(itemReader.readItems(ITEMS_FILE, promotions));
            } catch (IOException e) {
                throw StoreException.from(ErrorMessage.FILE_PROBLEM);
            }
        }
    }
}
