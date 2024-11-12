package store.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.exception.ErrorMessage.INVALID_ITEM;
import static store.exception.ErrorMessage.INVALID_QUANTITY;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.dto.response.PromotionResponseDto;
import store.exception.StoreException;

class ItemsTest {

    private Items items;

    @BeforeEach
    void init() {
        LocalDateTime startDate = LocalDateTime.of(2024, 10, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 11, 1, 0, 0, 0);
        Promotion promotion = new Promotion("1+1", 1, 1, startDate, endDate);
        Item item1 = new Item("a", 1000, 10, promotion);
        Item item2 = new Item("a", 1000, 10, null);
        Item item3 = new Item("b", 1000, 10, null);
        List<Item> itemList = new ArrayList<>(List.of(item1, item2, item3));
        items = new Items(itemList);
    }

    @Test
    @DisplayName("validatePurchase - 아이템 이름이 유효하지 않은 경우 예외가 발생한다.")
    void hasNoItem() {
        // given
        String name = "d";
        int count = 10;
        // when & then
        assertThatThrownBy(() -> items.validatePurchase(name, count))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(INVALID_ITEM.getMessage());
    }

    @Test
    @DisplayName("validatePurchase - 창고에 있는 아이템의 개수보다 크면 예외가 발생한다.")
    void moreThanStockQuantity() {
        // given
        String name = "a";
        int count = 21;
        // when & then
        assertThatThrownBy(() -> items.validatePurchase(name, count))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(INVALID_QUANTITY.getMessage());
    }

    @Test
    @DisplayName("purchaseItem - 창고에 없는 아이템을 구매하려고 하면 예외가 발생한다.")
    void purchaseNoItem() {
        // given
        String name = "d";
        int count = 10;
        // when & then
        assertThatThrownBy(() -> items.purchaseItem(name, count))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(INVALID_ITEM.getMessage());
    }

    @Test
    @DisplayName("purchaseItem - 창고에 있는 아이템 개수보다 많이 구매하려고 하면 예외가 발생한다.")
    void purchaseMoreThanStockQuantity() {
        // given
        String name = "a";
        int count = 21;
        // when & then
        assertThatThrownBy(() -> items.purchaseItem(name, count))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(INVALID_QUANTITY.getMessage());
    }

    @ParameterizedTest
    @CsvSource({"7,3,10", "10,0,10", "14,0,6", "20,0,0"})
    @DisplayName("purchaseItem - 프로모션이 있는 아이템을 우선으로 차감한다.")
    void purchaseHasPromotionItemFirst(int purchase, int expectedPromotionCount, int expectedUnPromotionCount) {
        // given
        String name = "a";
        // when
        items.purchaseItem(name, purchase);
        // then
        List<Item> itemList = items.getItems();
        Item itemAWithPromotion = itemList.stream()
                .filter(item -> item.getName().equals(name) && item.hasPromotionEvent()).findFirst().get();
        Item itemAWithoutPromotion = itemList.stream()
                .filter(item -> item.getName().equals(name) && !item.hasPromotionEvent()).findFirst().get();
        assertThat(itemAWithPromotion.getQuantity()).isEqualTo(expectedPromotionCount);
        assertThat(itemAWithoutPromotion.getQuantity()).isEqualTo(expectedUnPromotionCount);
    }

    @Test
    @DisplayName("calculateItemsPrice - 창고에 없는 아이템일 경우 예외가 발생한다.")
    void noItemInStore() {
        // given
        String name = "d";
        int count = 10;
        // when & then
        assertThatThrownBy(() -> items.calculateItemsPrice(name, count))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(INVALID_ITEM.getMessage());
    }

    @Test
    @DisplayName("calculateItemsPrice - 창고에 있는 아이템일 경우 가격을 계산한다.")
    void calculateItemInStore() {
        // given
        String name = "a";
        int count = 10;
        // when
        int result = items.calculateItemsPrice(name, count);
        // then
        assertThat(result).isEqualTo(10000);
    }

    @Test
    @DisplayName("getPromotionResult - 창고에 없는 경우 예외가 발생한다.")
    void getPromotionResultWhenNoItem() {
        // given
        String name = "d";
        int count = 10;
        LocalDateTime orderDate = LocalDateTime.of(2024, 10, 10, 0, 0, 0);
        // when & then
        assertThatThrownBy(() -> items.getPromotionResult(name, count, orderDate))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(INVALID_ITEM.getMessage());
    }

    @Test
    @DisplayName("getPromotionResult - 프로모션이 없는 상품인 경우에 PromotionResult는 NONE이다.")
    void getPromotionResultWhenNoPromotion() {
        // given
        String name = "b";
        int count = 10;
        LocalDateTime orderDate = LocalDateTime.of(2024, 10, 10, 0, 0, 0);
        // when
        PromotionResponseDto promotionResult = items.getPromotionResult(name, count, orderDate);
        // then
        assertThat(promotionResult.result()).isEqualTo(PromotionResult.NONE);
    }

    @Test
    @DisplayName("getPromotionResult - 프로모션이 있지만 해당 기간이 아닌 상품인 경우에 PromotionResult는 NONE이다.")
    void getPromotionResultWhenNotEventDay() {
        // given
        String name = "a";
        int count = 10;
        LocalDateTime orderDate = LocalDateTime.of(2024, 12, 10, 0, 0, 0);
        // when
        PromotionResponseDto promotionResult = items.getPromotionResult(name, count, orderDate);
        // then
        assertThat(promotionResult.result()).isEqualTo(PromotionResult.NONE);
    }

    @Test
    @DisplayName("getPromotionResult - 프로모션 재고보다 많이 판매된 경우에 PromotionResult는 APPLY_REGULAR_PRICE이다.")
    void getPromotionResultWhenBuyMoreThanStockQuantity() {
        // given
        String name = "a";
        int count = 15;
        LocalDateTime orderDate = LocalDateTime.of(2024, 10, 10, 0, 0, 0);
        // when
        PromotionResponseDto promotionResult = items.getPromotionResult(name, count, orderDate);
        // then
        assertThat(promotionResult.result()).isEqualTo(PromotionResult.APPLY_REGULAR_PRICE);
    }

    @Test
    @DisplayName("getPromotionResult - 추가로 손님이 제품을 가져와야하는 경우에 PromotionResult는 REQUIRE_ADDITIONAL_ITEM이다.")
    void getPromotionResultWhenCustomerShouldGetMore() {
        // given
        String name = "a";
        int count = 1;
        LocalDateTime orderDate = LocalDateTime.of(2024, 10, 10, 0, 0, 0);
        // when
        PromotionResponseDto promotionResult = items.getPromotionResult(name, count, orderDate);
        // then
        assertThat(promotionResult.result()).isEqualTo(PromotionResult.REQUIRE_ADDITIONAL_ITEM);
    }

    @Test
    @DisplayName("getPromotionResult - 정상적으로 아이템을 들고왔을 경우 PromotionResult는 SUCCESS이다.")
    void getPromotionResultWhenSuccess() {
        // given
        String name = "a";
        int count = 2;
        LocalDateTime orderDate = LocalDateTime.of(2024, 10, 10, 0, 0, 0);
        // when
        PromotionResponseDto promotionResult = items.getPromotionResult(name, count, orderDate);
        // then
        assertThat(promotionResult.result()).isEqualTo(PromotionResult.SUCCESS);
    }
}