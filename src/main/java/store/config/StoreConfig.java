package store.config;

import java.io.IOException;
import java.util.List;
import store.controller.OrderController;
import store.domain.Item;
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
    public OrderService orderService() {
        Items items = Initialize.initializeItems();
        return new OrderService(items);
    }

    public OutputView outputView() {
        return new OutputView();
    }

    public InputView inputView() {
        return new InputView();
    }

    public OrderController orderController() {
        return new OrderController(outputView(), inputView(), orderService());
    }

    private static class Initialize {
        private static final String ITEMS_FILE = "src/main/resources/products.md";
        private static final String PROMOTION_FILE = "src/main/resources/promotions.md";

        public static Items initializeItems() throws StoreException {
            try {
                PromotionReader promotionReader = new PromotionReader();
                ItemReader itemReader = new ItemReader();
                List<Promotion> promotions = promotionReader.readPromotions(PROMOTION_FILE);
                List<Item> itemList = itemReader.readItems(ITEMS_FILE, promotions);
                return new Items(itemList);
            } catch (IOException e) {
                throw StoreException.from(ErrorMessage.FILE_PROBLEM);
            }
        }
    }
}
