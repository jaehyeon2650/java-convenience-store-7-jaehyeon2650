package store.dto.response;

import store.domain.PromotionResult;

public record PromotionInfoResponseDto(
        String name,
        PromotionResponseDto promotionResponseDto
) {
    public PromotionResult getPromotionResult() {
        return promotionResponseDto.result();
    }
}
