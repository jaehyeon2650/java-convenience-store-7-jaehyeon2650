package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.ArrayList;
import java.util.List;
import store.domain.PromotionResult;
import store.dto.request.OrderRequestDto;
import store.dto.request.OrdersRequestDto;
import store.dto.request.PaymentRequestDto;
import store.dto.response.PromotionInfoResponseDto;
import store.dto.response.PromotionResponseDto;
import store.dto.response.ReceiptsResponseDto;
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


    public void run() {
        boolean rotate = true;
        while (rotate) {
            outputView.printHelloMessage();
            outputView.printItemList(orderService.getItemList());
            List<PromotionInfoResponseDto> infoResponseDtos = makeOrderInfo();
            List<PaymentRequestDto> paymentRequestDtos = makePaymentsRequest(infoResponseDtos);
            boolean memberShip = checkMemberShip();
            getReceipt(paymentRequestDtos, memberShip);
            rotate = checkRotate();
        }
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

    private List<PaymentRequestDto> makePaymentsRequest(List<PromotionInfoResponseDto> promotionInfoResponseDtos) {
        List<PaymentRequestDto> paymentRequestDtos = new ArrayList<>();
        for (PromotionInfoResponseDto promotionInfoResponseDto : promotionInfoResponseDtos) {
            paymentRequestDtos.add(makePaymentRequest(promotionInfoResponseDto));
        }
        return paymentRequestDtos;
    }

    private PaymentRequestDto makePaymentRequest(PromotionInfoResponseDto promotionInfoResponseDto) {
        PromotionResponseDto promotion = promotionInfoResponseDto.promotionResponseDto();
        String name = promotionInfoResponseDto.name();
        if (promotionInfoResponseDto.getPromotionResult() == PromotionResult.APPLY_REGULAR_PRICE) {
            if (checkExtraPayment(name, promotion.extraBuy())) {
                return PaymentRequestDto.of(name, promotion.buyCount() + promotion.extraBuy(), promotion.getCount());
            }
            return PaymentRequestDto.of(name, promotion.buyCount(), promotion.getCount());
        }
        if (promotionInfoResponseDto.getPromotionResult() == PromotionResult.REQUIRE_ADDITIONAL_ITEM) {
            if (checkAddFreeItem(name)) {
                return PaymentRequestDto.of(name, promotion.buyCount(), promotion.getCount() + promotion.extraGet());
            }
        }
        return PaymentRequestDto.of(name, promotion.buyCount(), promotion.getCount());
    }

    private void getReceipt(List<PaymentRequestDto> paymentRequestDtos, boolean membership) {
        ReceiptsResponseDto receipts = orderService.payment(paymentRequestDtos, membership);
        outputView.printReceipts(receipts);
    }

    private boolean checkMemberShip() {
        while (true) {
            try {
                return inputView.chooseMemberShip();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean checkAddFreeItem(String name) {
        while (true) {
            try {
                return inputView.chooseAddFreeItem(name);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean checkExtraPayment(String name, int count) {
        while (true) {
            try {
                return inputView.chooseExtraPayment(name, count);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean checkRotate() {
        while (true) {
            try {
                return inputView.chooseRotate();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
