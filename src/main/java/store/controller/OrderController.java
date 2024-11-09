package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.ArrayList;
import java.util.List;
import store.domain.PromotionResult;
import store.dto.response.PromotionInfoResponseDto;
import store.dto.response.PromotionResponseDto;
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

    private List<PromotionInfoResponseDto> makeOrderInfo() {
        while (true) {
            try {
                List<PromotionInfoResponseDto> infoResponseDtos = new ArrayList<>();
                OrdersRequestDto orders = inputView.getOrders();
                List<OrderRequestDto> orderRequestDtos = orders.orderList();
                for (OrderRequestDto orderRequestDto : orderRequestDtos) {
                    infoResponseDtos.add(orderService.getOrderInfo(orderRequestDto.name(),
                            orderRequestDto.quantity(),
                            DateTimes.now()));
                }
                return infoResponseDtos;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
