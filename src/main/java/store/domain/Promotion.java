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

    public String getName() {
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

    public boolean canApplyPromotion(LocalDateTime time) {
        return startDate.isBefore(time) && endDate.isAfter(time);
    }

    public PromotionResponseDto getPromotionResult(int stockQuantity, int purchase) {
        if (stockQuantity > purchase && purchase % (buy + get) == buy) {
            int set = purchase / (buy + get);
            int buyCount = set * buy + buy;
            int getCount = set * get;
            int extraBuy = 0;
            int extraGet = 1;
            return PromotionResponseDto.of(REQUIRE_ADDITIONAL_ITEM, buyCount, getCount, extraBuy, extraGet);
        } else if (stockQuantity < purchase || (stockQuantity == purchase && purchase % (get + buy) == buy)) {
            int set = stockQuantity / (buy + get);
            int buyCount = set * buy;
            int getCount = set * get;
            int extraGet = 0;
            int extraBuy = (purchase - stockQuantity) + (stockQuantity) % (buy + get);
            return PromotionResponseDto.of(APPLY_REGULAR_PRICE, buyCount, getCount, extraBuy, extraGet);
        } else {
            int set = purchase / (buy + get);
            int buyCount = set * buy + (purchase) % (buy + get);
            int getCount = set * get;
            int extraBuy = 0;
            int extraGet = 0;
            return PromotionResponseDto.of(SUCCESS, buyCount, getCount, extraBuy, extraGet);
        }
    }

}
