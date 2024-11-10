package store.domain;

import static store.domain.PromotionResult.APPLY_REGULAR_PRICE;
import static store.domain.PromotionResult.REQUIRE_ADDITIONAL_ITEM;
import static store.domain.PromotionResult.SUCCESS;

import java.time.LocalDateTime;
import java.util.Objects;
import store.dto.response.PromotionResponseDto;

public class Promotion {
    private String name;
    private int buy;
    private int get;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Promotion(String name, int buy, int get, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getPromotionName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Promotion promotion = (Promotion) o;
        return Objects.equals(name, promotion.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    private boolean canApplyPromotion(LocalDateTime time) {
        return startDate.isBefore(time) && endDate.isAfter(time);
    }

    public PromotionResponseDto getPromotionResult(int stockQuantity, int purchase, LocalDateTime orderDate) {
        if (!canApplyPromotion(orderDate)) {
            return createFailResponse(purchase);
        }
        if (stockQuantity > purchase && purchase % (buy + get) == buy) {
            return createAdditionalPurchaseRequirementResponse(purchase);
        }
        if (stockQuantity < purchase || (stockQuantity == purchase && purchase % (get + buy) == buy)) {
            return createRegularPriceRequirementResponse(stockQuantity, purchase);
        }
        return createSuccessResponse(purchase);
    }

    private PromotionResponseDto createFailResponse(int purchase) {
        return new PromotionResponseDto(PromotionResult.NONE, purchase, 0, 0, 0);
    }

    private PromotionResponseDto createSuccessResponse(int purchase) {
        int set = purchase / (buy + get);
        int buyCount = set * buy + purchase % (buy + get);
        int getCount = set * get;
        int extraBuy = 0;
        int extraGet = 0;
        return new PromotionResponseDto(SUCCESS, buyCount, getCount, extraBuy, extraGet);
    }

    private PromotionResponseDto createRegularPriceRequirementResponse(int stockQuantity, int purchase) {
        int set = stockQuantity / (buy + get);
        int buyCount = set * buy;
        int getCount = set * get;
        int extraGet = 0;
        int extraBuy = (purchase - stockQuantity) + stockQuantity % (buy + get);
        return new PromotionResponseDto(APPLY_REGULAR_PRICE, buyCount, getCount, extraBuy, extraGet);
    }

    private PromotionResponseDto createAdditionalPurchaseRequirementResponse(int purchase) {
        int set = purchase / (buy + get);
        int buyCount = set * buy + buy;
        int getCount = set * get;
        int extraBuy = 0;
        int extraGet = 1;
        return new PromotionResponseDto(REQUIRE_ADDITIONAL_ITEM, buyCount, getCount, extraBuy, extraGet);
    }

}
