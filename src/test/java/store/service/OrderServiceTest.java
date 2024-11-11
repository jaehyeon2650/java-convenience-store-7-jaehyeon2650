package store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Item;
import store.domain.Items;
import store.domain.Promotion;
import store.domain.PromotionResult;
import store.dto.request.PaymentRequestDto;
import store.dto.response.ItemResponseDto;
import store.dto.response.ItemsResponseDto;
import store.dto.response.PromotionInfoResponseDto;
import store.dto.response.ReceiptsResponseDto;
import store.exception.ErrorMessage;
import store.exception.StoreException;

class OrderServiceTest {
    private OrderService orderService;

    @BeforeEach
    void init() {
        LocalDateTime startDate = LocalDateTime.of(2024, 10, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 11, 1, 0, 0, 0);
        Promotion promotion = new Promotion("1+1", 1, 1, startDate, endDate);
        Item item1 = new Item("a", 1000, 10, promotion);
        Item item2 = new Item("a", 1000, 10, null);
        Item item3 = new Item("b", 1000, 1000, null);
        List<Item> itemList = new ArrayList<>(List.of(item1, item2, item3));
        Items items = new Items(itemList);
        orderService = new OrderService(items);
    }

    @Test
    @DisplayName("getItemList - 정확한 아이템들의 정보를 가져온다.")
    void getItemList() {
        // when
        ItemsResponseDto items = orderService.getItemList();
        // then
        List<ItemResponseDto> itemList = items.itemResponses();
        boolean hasItemAWithPromotion = itemList.stream()
                .anyMatch(itemResponseDto -> itemResponseDto.name().equals("a") && itemResponseDto.quantity() == 10
                        && itemResponseDto.price() == 1000 && itemResponseDto.promotion().isPresent());
        boolean hasItemAWithoutPromotion = itemList.stream()
                .anyMatch(itemResponseDto -> itemResponseDto.name().equals("a") && itemResponseDto.quantity() == 10
                        && itemResponseDto.price() == 1000 && itemResponseDto.promotion().isEmpty());
        boolean hasItemB = itemList.stream()
                .anyMatch(itemResponseDto -> itemResponseDto.name().equals("b") && itemResponseDto.quantity() == 1000
                        && itemResponseDto.price() == 1000 && itemResponseDto.promotion().isEmpty());
        assertTrue(hasItemAWithPromotion);
        assertTrue(hasItemAWithoutPromotion);
        assertTrue(hasItemB);
    }

    @Test
    @DisplayName("getOrderInfo - 해당 아이템이 없는 경우 예외가 발생한다.")
    void noItem() {
        // given
        String name = "c";
        int count = 7;
        LocalDateTime orderDate = LocalDateTime.of(2024, 10, 10, 0, 0, 0);
        // when & then
        assertThatThrownBy(() -> orderService.getOrderInfo(name, count, orderDate))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(ErrorMessage.INVALID_ITEM.getMessage());
    }

    @Test
    @DisplayName("getOrderInfo - 재고 수량보다 많이 사는 경우 예외가 발생한다.")
    void noStockQuantity() {
        // given
        String name = "a";
        int count = 21;
        LocalDateTime orderDate = LocalDateTime.of(2024, 10, 10, 0, 0, 0);
        // when & then
        assertThatThrownBy(() -> orderService.getOrderInfo(name, count, orderDate))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(ErrorMessage.INVALID_QUANTITY.getMessage());
    }

    @Test
    @DisplayName("getOrderInfo - 프로모션이 없는 상품인 경우에 PromotionResult는 NONE이다.")
    void noPromotion() {
        // given
        String name = "b";
        int count = 7;
        LocalDateTime orderDate = LocalDateTime.of(2024, 10, 10, 0, 0, 0);
        // when
        PromotionInfoResponseDto result = orderService.getOrderInfo(name, count, orderDate);
        // then
        assertThat(result.getPromotionResult()).isEqualTo(PromotionResult.NONE);
        assertThat(result.name()).isEqualTo(name);
    }

    @Test
    @DisplayName("getOrderInfo - 프로모션이 있지만 해당 기간이 아닌 상품인 경우에 PromotionResult는 NONE이다.")
    void getPromotionResultWhenNotEventDay() {
        // given
        String name = "a";
        int count = 7;
        LocalDateTime orderDate = LocalDateTime.of(2024, 12, 10, 0, 0, 0);
        // when
        PromotionInfoResponseDto result = orderService.getOrderInfo(name, count, orderDate);
        // then
        assertThat(result.getPromotionResult()).isEqualTo(PromotionResult.NONE);
        assertThat(result.name()).isEqualTo(name);
    }

    @Test
    @DisplayName("getOrderInfo - 프로모션 재고보다 많이 판매된 경우에 PromotionResult는 APPLY_REGULAR_PRICE이다.")
    void getPromotionResultWhenBuyMoreThanStockQuantity() {
        // given
        String name = "a";
        int count = 11;
        LocalDateTime orderDate = LocalDateTime.of(2024, 10, 10, 0, 0, 0);
        // when
        PromotionInfoResponseDto result = orderService.getOrderInfo(name, count, orderDate);
        // then
        assertThat(result.getPromotionResult()).isEqualTo(PromotionResult.APPLY_REGULAR_PRICE);
        assertThat(result.name()).isEqualTo(name);
    }

    @Test
    @DisplayName("getOrderInfo - 추가로 손님이 제품을 가져와야하는 경우에 PromotionResult는 REQUIRE_ADDITIONAL_ITEM이다.")
    void getPromotionResultWhenCustomerShouldGetMore() {
        // given
        String name = "a";
        int count = 1;
        LocalDateTime orderDate = LocalDateTime.of(2024, 10, 10, 0, 0, 0);
        // when
        PromotionInfoResponseDto result = orderService.getOrderInfo(name, count, orderDate);
        // then
        assertThat(result.getPromotionResult()).isEqualTo(PromotionResult.REQUIRE_ADDITIONAL_ITEM);
        assertThat(result.name()).isEqualTo(name);
    }

    @Test
    @DisplayName("getOrderInfo - 정상적으로 아이템을 들고왔을 경우 PromotionResult는 SUCCESS이다.")
    void getPromotionResultWhenSuccess() {
        // given
        String name = "a";
        int count = 8;
        LocalDateTime orderDate = LocalDateTime.of(2024, 10, 10, 0, 0, 0);
        // when
        PromotionInfoResponseDto result = orderService.getOrderInfo(name, count, orderDate);
        // then
        assertThat(result.getPromotionResult()).isEqualTo(PromotionResult.SUCCESS);
        assertThat(result.name()).isEqualTo(name);
    }

    @Test
    @DisplayName("payment - 정상적으로 아이템의 개수가 줄어들어든다. 프로모션이 있는 상품부터 개수가 줄어든다.")
    void reduceItem() {
        // given
        List<PaymentRequestDto> paymentRequestDto = createPaymentRequestDto();
        boolean membership = true;
        // when
        orderService.payment(paymentRequestDto, membership);
        // then
        ItemsResponseDto items = orderService.getItemList();
        List<ItemResponseDto> itemList = items.itemResponses();
        boolean hasItemAWithPromotion = itemList.stream()
                .anyMatch(itemResponseDto -> itemResponseDto.name().equals("a") && itemResponseDto.quantity() == 0
                        && itemResponseDto.price() == 1000 && itemResponseDto.promotion().isPresent());
        boolean hasItemAWithoutPromotion = itemList.stream()
                .anyMatch(itemResponseDto -> itemResponseDto.name().equals("a") && itemResponseDto.quantity() == 5
                        && itemResponseDto.price() == 1000 && itemResponseDto.promotion().isEmpty());
        boolean hasItemB = itemList.stream()
                .anyMatch(itemResponseDto -> itemResponseDto.name().equals("b") && itemResponseDto.quantity() == 993
                        && itemResponseDto.price() == 1000 && itemResponseDto.promotion().isEmpty());
        assertTrue(hasItemAWithPromotion);
        assertTrue(hasItemAWithoutPromotion);
        assertTrue(hasItemB);
    }

    @Test
    @DisplayName("payment - 맴버십 할인이 있는 경우 할인을 고려해서 영수증을 계산한다.")
    void hasMembership() {
        // given
        List<PaymentRequestDto> paymentRequestDto = createPaymentRequestDto();
        boolean membership = true;
        // when
        ReceiptsResponseDto payment = orderService.payment(paymentRequestDto, membership);
        // then
        assertThat(payment.membershipDiscount()).isEqualTo(2100);
        assertThat(payment.promotionDiscount()).isEqualTo(5000);
        assertThat(payment.totalCount()).isEqualTo(22);
        assertThat(payment.totalPrice()).isEqualTo(22000);
        assertThat(payment.payment()).isEqualTo(14900);
    }

    @Test
    @DisplayName("payment - 맴버십 할인이 없는 경우 맴버십 할인을 제외하고 영수증을 계산한다.")
    void noMembership() {
        // given
        List<PaymentRequestDto> paymentRequestDto = createPaymentRequestDto();
        boolean membership = false;
        // when
        ReceiptsResponseDto payment = orderService.payment(paymentRequestDto, membership);
        // then
        assertThat(payment.membershipDiscount()).isEqualTo(0);
        assertThat(payment.promotionDiscount()).isEqualTo(5000);
        assertThat(payment.totalCount()).isEqualTo(22);
        assertThat(payment.totalPrice()).isEqualTo(22000);
        assertThat(payment.payment()).isEqualTo(17000);
    }

    @Test
    @DisplayName("payment - 맴버십 할인은 최대 8000원 까지 가능하다.")
    void membershipDiscountMax() {
        // given
        List<PaymentRequestDto> paymentRequestDto = new ArrayList<>();
        PaymentRequestDto request1 = new PaymentRequestDto("a", 10, 5);
        PaymentRequestDto request2 = new PaymentRequestDto("b", 100, 0);
        paymentRequestDto.add(request1);
        paymentRequestDto.add(request2);
        boolean membership = true;
        // when
        ReceiptsResponseDto payment = orderService.payment(paymentRequestDto, membership);
        // then
        assertThat(payment.membershipDiscount()).isEqualTo(8000);
        assertThat(payment.promotionDiscount()).isEqualTo(5000);
        assertThat(payment.totalCount()).isEqualTo(115);
        assertThat(payment.totalPrice()).isEqualTo(115000);
        assertThat(payment.payment()).isEqualTo(102000);
    }


    private List<PaymentRequestDto> createPaymentRequestDto() {
        List<PaymentRequestDto> list = new ArrayList<>();
        PaymentRequestDto request1 = new PaymentRequestDto("a", 10, 5);
        PaymentRequestDto request2 = new PaymentRequestDto("b", 7, 0);
        list.add(request1);
        list.add(request2);
        return list;
    }
}