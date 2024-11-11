package store.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.exception.ErrorMessage.INVALID_QUANTITY;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.exception.StoreException;

class ItemTest {
    @Test
    @DisplayName("getPromotionName - 프로모션이 있는 경우 정확히 프로모션 이름을 출력한다.")
    void successGetPromotionName() {
        // given
        Item item = createHasPromotionItem();
        // when
        Optional<String> promotionName = item.getPromotionName();
        // then
        assertThat(promotionName.isPresent()).isTrue();
        assertThat(promotionName.get()).isEqualTo("1+1");
    }

    @Test
    @DisplayName("getPromotionName - 프로모션이 없는 경우 빈 Optional 객체를 반환한다.")
    void failGetPromotionName() {
        // given
        Item item = createHasntPromotionItem();
        // when
        Optional<String> promotionName = item.getPromotionName();
        // then
        assertThat(promotionName.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @CsvSource({"2,2000", "3,3000", "4,4000", "10,10000", "0,0"})
    @DisplayName("calculatePrice - 개수가 주어졌을 때 총 가격을 정확히 반환한다.")
    void calculatePrice(int count, int expectedPrice) {
        // given
        Item item = createHasPromotionItem();
        // when
        int price = item.calculatePrice(count);
        // then
        assertThat(price).isEqualTo(expectedPrice);
    }

    @ParameterizedTest
    @CsvSource({"1,9", "2,8", "3,7", "4,6", "5,5", "6,4", "7,3", "8,2", "9,1", "10,0"})
    @DisplayName("purchaseItem - 재고 수량보다 적게 구매를 요청하면 정상적으로 재고 수량이 줄어든다.")
    void successPurchaseItem(int purchase, int expectedQuantity) {
        // given
        Item item = createHasPromotionItem();
        // when
        item.purchaseItem(purchase);
        // then
        assertThat(item.getQuantity()).isEqualTo(expectedQuantity);
    }

    @Test
    @DisplayName("purchaseItem - 재고 수량보다 많게 구매를 하면 예외가 발생한다.")
    void overQuantityPurchase() {
        // given
        Item item = createHasPromotionItem();
        // when & then
        assertThatThrownBy(() -> item.purchaseItem(11))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(INVALID_QUANTITY.getMessage());
    }

    @Test
    @DisplayName("Item들을 정렬하면 프로모션이 있는 Item이 우선적으로 정렬되어야한다.")
    void sort() {
        // given
        List<Item> items = new ArrayList<>(
                List.of(createHasntPromotionItem(), createHasPromotionItem(), createHasntPromotionItem(),
                        createHasPromotionItem()));
        // when
        Collections.sort(items);
        // then
        assertThat(items.get(0).getPromotionName()).isPresent();
        assertThat(items.get(1).getPromotionName()).isPresent();
        assertThat(items.get(2).getPromotionName()).isEmpty();
        assertThat(items.get(3).getPromotionName()).isEmpty();
    }

    private Item createHasPromotionItem() {
        LocalDateTime startDate = LocalDateTime.of(2024, 10, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 11, 1, 0, 0, 0);
        return new Item("aaa", 1000, 10,
                new Promotion("1+1", 1, 1, startDate, endDate));
    }

    private Item createHasntPromotionItem() {
        LocalDateTime startDate = LocalDateTime.of(2024, 10, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 11, 1, 0, 0, 0);
        return new Item("aaa", 1000, 10,
                null);
    }
}