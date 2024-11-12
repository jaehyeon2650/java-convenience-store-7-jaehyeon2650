package store.dto.response;

import store.domain.PromotionResult;

public record PromotionResponseDto(
        PromotionResult result,
        int buyCount,
        int getCount,
        int extraBuy,
        int extraGet
) {
}
