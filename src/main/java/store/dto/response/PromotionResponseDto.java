package store.dto.response;

import store.domain.PromotionResult;

public record PromotionResponseDto(
        PromotionResult result,
        int buyCount,
        int getCount,
        int extraBuy,
        int extraGet
) {
    public static PromotionResponseDto of(PromotionResult result, int buyCount, int getCount, int extraBuy,
                                          int extraGet) {
        return new PromotionResponseDto(result, buyCount, getCount, extraBuy, extraGet);
    }
}
