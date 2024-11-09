package store.controller;

import store.service.OrderService;
import store.view.InputView;
import store.view.OutputView;

public class OrderController {
    private final OutputView outputView;
    private final InputView inputView;
    private final OrderService orderService;

    public OrderController(OutputView outputView, InputView inputView, OrderService orderService) {
        this.outputView = outputView;
        this.inputView = inputView;
        this.orderService = orderService;
    }
}
