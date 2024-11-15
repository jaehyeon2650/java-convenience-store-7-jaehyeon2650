package store.domain;

import java.time.LocalDateTime;
import store.dto.response.PromotionResponseDto;

public class Promotion {
    private final String name;
    private final int buy;
    private final int get;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

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

    public boolean canApplyPromotion(LocalDateTime time) {
        return startDate.isBefore(time) && endDate.isAfter(time);
    }

    public PromotionResponseDto getPromotionResult(int stockQuantity, int purchase) {
        for (PromotionResult result : PromotionResult.values()) {
            if (result.canApply(buy, get, stockQuantity, purchase)) {
                return result.getResult(buy, get, stockQuantity, purchase);
            }
        }
        return PromotionResult.NONE.getResult(buy, get, stockQuantity, purchase);
    }
}
