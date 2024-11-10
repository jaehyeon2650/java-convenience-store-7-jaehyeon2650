package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
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
            List<PromotionInfoResponseDto> infoResponseDtos = retryAboutInvalidInput(this::makeOrderInfo);
            List<PaymentRequestDto> paymentRequestDtos = makePaymentsRequest(infoResponseDtos);
            boolean memberShip = retryAboutInvalidInput(this::checkMemberShip);
            createAndShowReceipt(paymentRequestDtos, memberShip);
            rotate = retryAboutInvalidInput(this::checkRotate);
        }
    }

    private List<PromotionInfoResponseDto> makeOrderInfo() {
        List<PromotionInfoResponseDto> infoResponseDtos = new ArrayList<>();
        OrdersRequestDto orders = inputView.getOrders();
        List<OrderRequestDto> orderRequestDtos = orders.orderList();
        for (OrderRequestDto orderRequestDto : orderRequestDtos) {
            infoResponseDtos.add(orderService.getOrderInfo(orderRequestDto.name(),
                    orderRequestDto.quantity(),
                    DateTimes.now()));
        }
        return infoResponseDtos;
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
            return createRequestWhenApplyRegualrPrice(name, promotion);
        }
        if (promotionInfoResponseDto.getPromotionResult() == PromotionResult.REQUIRE_ADDITIONAL_ITEM) {
            return createRequestWhenRequireAdditionalItem(name, promotion);
        }
        return new PaymentRequestDto(name, promotion.buyCount(), promotion.getCount());
    }

    private PaymentRequestDto createRequestWhenRequireAdditionalItem(String name, PromotionResponseDto promotion) {
        if (retryAboutInvalidInput(this::checkAddFreeItem, name, promotion.extraGet())) {
            return new PaymentRequestDto(name, promotion.buyCount(), promotion.getCount() + promotion.extraGet());
        }
        return new PaymentRequestDto(name, promotion.buyCount(), promotion.getCount());
    }

    private PaymentRequestDto createRequestWhenApplyRegualrPrice(String name, PromotionResponseDto promotion) {
        if (retryAboutInvalidInput(this::checkExtraPayment, name, promotion.extraBuy())) {
            return new PaymentRequestDto(name, promotion.buyCount() + promotion.extraBuy(), promotion.getCount());
        }
        return new PaymentRequestDto(name, promotion.buyCount(), promotion.getCount());
    }

    private void createAndShowReceipt(List<PaymentRequestDto> paymentRequestDtos, boolean membership) {
        ReceiptsResponseDto receipts = orderService.payment(paymentRequestDtos, membership);
        outputView.printReceipts(receipts);
    }

    private boolean checkMemberShip() {
        return inputView.chooseMemberShip();
    }

    private boolean checkAddFreeItem(String name, int count) {
        return inputView.chooseAddFreeItem(name, count);
    }

    private boolean checkExtraPayment(String name, int count) {
        return inputView.chooseExtraPayment(name, count);
    }

    private boolean checkRotate() {
        return inputView.chooseRotate();
    }

    private <T> T retryAboutInvalidInput(Supplier<T> supplier) {
        while (true) {
            try {
                return supplier.get();
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private <T, U, R> R retryAboutInvalidInput(BiFunction<T, U, R> function, T arg1, U arg2) {
        while (true) {
            try {
                return function.apply(arg1, arg2);
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }
}
