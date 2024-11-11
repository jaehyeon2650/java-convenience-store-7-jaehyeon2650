package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.dto.response.PromotionResponseDto;

class PromotionTest {
    @Test
    @DisplayName("getPromotionResult(2+1) - 프로모션 아이템 재고보다 많이 구매하려는 경우 일반 결제할 상품이 생긴다.")
    void moreThanStockQuantityWhen2plus1() {
        // given
        Promotion promotion = make2Plus1Promotion();
        // when
        PromotionResponseDto promotionResult = promotion.getPromotionResult(10, 11);
        // then
        assertThat(promotionResult.result()).isEqualTo(PromotionResult.APPLY_REGULAR_PRICE);
        assertThat(promotionResult.buyCount()).isEqualTo(6);
        assertThat(promotionResult.getCount()).isEqualTo(3);
        assertThat(promotionResult.extraGet()).isEqualTo(0);
        assertThat(promotionResult.extraBuy()).isEqualTo(2);
    }

    @ParameterizedTest
    @CsvSource({"2,2,0,0,2", "5,5,2,1,2", "8,8,4,2,2"})
    @DisplayName("getPromotionResult(2+1) - 추가 증정을 하려고 했으나 재고가 부족한 경우 일반 결제할 상품이 생긴다.")
    void noStockQuantityWhenCustomerMistakeWhen2plus1(int stockQuantity, int purchase, int expectedBuyCount,
                                                      int expectedGetCount,
                                                      int expectedExtraBuy) {
        // given
        Promotion promotion = make2Plus1Promotion();
        // when
        PromotionResponseDto promotionResult = promotion.getPromotionResult(stockQuantity, purchase);
        // then
        assertThat(promotionResult.result()).isEqualTo(PromotionResult.APPLY_REGULAR_PRICE);
        assertThat(promotionResult.buyCount()).isEqualTo(expectedBuyCount);
        assertThat(promotionResult.getCount()).isEqualTo(expectedGetCount);
        assertThat(promotionResult.extraGet()).isEqualTo(0);
        assertThat(promotionResult.extraBuy()).isEqualTo(expectedExtraBuy);
    }

    @ParameterizedTest
    @CsvSource({"2,2,0,1", "5,4,1,1", "8,6,2,1"})
    @DisplayName("getPromotionResult(2+1) - 손님께서 프로모션 상품에 대해 추가 증정품이 안가져온 경우")
    void whenCustomerMistakePromotionWhen2plus1(int purchase, int expectedBuyCount, int expectedGetCount,
                                                int expectedExtraGet) {
        // given
        Promotion promotion = make2Plus1Promotion();
        // when
        PromotionResponseDto promotionResult = promotion.getPromotionResult(10, purchase);
        // then
        assertThat(promotionResult.result()).isEqualTo(PromotionResult.REQUIRE_ADDITIONAL_ITEM);
        assertThat(promotionResult.buyCount()).isEqualTo(expectedBuyCount);
        assertThat(promotionResult.getCount()).isEqualTo(expectedGetCount);
        assertThat(promotionResult.extraGet()).isEqualTo(expectedExtraGet);
        assertThat(promotionResult.extraBuy()).isEqualTo(0);
    }

    @ParameterizedTest
    @CsvSource({"7,5,2", "9,6,3", "10,7,3"})
    @DisplayName("getPromotionResult(2+1) - 정상적으로 손님이 프로모션 구매 상품에 대해 구매한 경우")
    void whenSuccessPromotionWhen2plus1(int purchase, int expectedBuyCount, int expectedGetCount) {
        // given
        Promotion promotion = make2Plus1Promotion();
        // when
        PromotionResponseDto promotionResult = promotion.getPromotionResult(10, purchase);
        // then
        assertThat(promotionResult.result()).isEqualTo(PromotionResult.SUCCESS);
        assertThat(promotionResult.buyCount()).isEqualTo(expectedBuyCount);
        assertThat(promotionResult.getCount()).isEqualTo(expectedGetCount);
        assertThat(promotionResult.extraGet()).isEqualTo(0);
        assertThat(promotionResult.extraBuy()).isEqualTo(0);
    }

    private Promotion make2Plus1Promotion() {
        LocalDateTime startDate = LocalDateTime.of(2024, 10, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 11, 1, 0, 0, 0);
        return new Promotion("반짝이벤트", 2, 1, startDate, endDate);
    }

    @Test
    @DisplayName("getPromotionResult(1+1) - 프로모션 아이템 재고보다 많이 구매하려는 경우 일반 결제할 상품이 생긴다.")
    void moreThanStockQuantityWhen1plus1() {
        // given
        Promotion promotion = make1Plus1Promotion();
        // when
        PromotionResponseDto promotionResult = promotion.getPromotionResult(10, 11);
        // then
        assertThat(promotionResult.result()).isEqualTo(PromotionResult.APPLY_REGULAR_PRICE);
        assertThat(promotionResult.buyCount()).isEqualTo(5);
        assertThat(promotionResult.getCount()).isEqualTo(5);
        assertThat(promotionResult.extraGet()).isEqualTo(0);
        assertThat(promotionResult.extraBuy()).isEqualTo(1);
    }

    @ParameterizedTest
    @CsvSource({"3,3,1,1,1", "5,5,2,2,1", "7,7,3,3,1"})
    @DisplayName("getPromotionResult(1+1) - 추가 증정을 하려고 했으나 재고가 부족한 경우 일반 결제할 상품이 생긴다.")
    void noStockQuantityWhenCustomerMistakeWhen1plus1(int stockQuantity, int purchase, int expectedBuyCount,
                                                      int expectedGetCount,
                                                      int expectedExtraBuy) {
        // given
        Promotion promotion = make1Plus1Promotion();
        // when
        PromotionResponseDto promotionResult = promotion.getPromotionResult(stockQuantity, purchase);
        // then
        assertThat(promotionResult.result()).isEqualTo(PromotionResult.APPLY_REGULAR_PRICE);
        assertThat(promotionResult.buyCount()).isEqualTo(expectedBuyCount);
        assertThat(promotionResult.getCount()).isEqualTo(expectedGetCount);
        assertThat(promotionResult.extraGet()).isEqualTo(0);
        assertThat(promotionResult.extraBuy()).isEqualTo(expectedExtraBuy);
    }

    @ParameterizedTest
    @CsvSource({"1,1,0,1", "3,2,1,1", "5,3,2,1"})
    @DisplayName("getPromotionResult(1+1) - 손님께서 프로모션 상품에 대해 추가 증정품이 안가져온 경우")
    void whenCustomerMistakePromotionWhen1plus1(int purchase, int expectedBuyCount, int expectedGetCount,
                                                int expectedExtraGet) {
        // given
        Promotion promotion = make1Plus1Promotion();
        // when
        PromotionResponseDto promotionResult = promotion.getPromotionResult(10, purchase);
        // then
        assertThat(promotionResult.result()).isEqualTo(PromotionResult.REQUIRE_ADDITIONAL_ITEM);
        assertThat(promotionResult.buyCount()).isEqualTo(expectedBuyCount);
        assertThat(promotionResult.getCount()).isEqualTo(expectedGetCount);
        assertThat(promotionResult.extraGet()).isEqualTo(expectedExtraGet);
        assertThat(promotionResult.extraBuy()).isEqualTo(0);
    }

    @ParameterizedTest
    @CsvSource({"6,3,3", "8,4,4", "10,5,5"})
    @DisplayName("getPromotionResult(1+1) - 정상적으로 손님이 프로모션 구매 상품에 대해 구매한 경우")
    void whenSuccessPromotionWhen1plus1(int purchase, int expectedBuyCount, int expectedGetCount) {
        // given
        Promotion promotion = make1Plus1Promotion();
        // when
        PromotionResponseDto promotionResult = promotion.getPromotionResult(10, purchase);
        // then
        assertThat(promotionResult.result()).isEqualTo(PromotionResult.SUCCESS);
        assertThat(promotionResult.buyCount()).isEqualTo(expectedBuyCount);
        assertThat(promotionResult.getCount()).isEqualTo(expectedGetCount);
        assertThat(promotionResult.extraGet()).isEqualTo(0);
        assertThat(promotionResult.extraBuy()).isEqualTo(0);
    }

    private Promotion make1Plus1Promotion() {
        LocalDateTime startDate = LocalDateTime.of(2024, 10, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 11, 1, 0, 0, 0);
        return new Promotion("1+1", 1, 1, startDate, endDate);
    }
}