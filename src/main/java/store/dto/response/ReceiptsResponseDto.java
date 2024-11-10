package store.dto.response;

import java.util.List;

public record ReceiptsResponseDto(
        int totalPrice,
        int totalCount,
        int promotionDiscount,
        int membershipDiscount,
        int payment,
        List<ReceiptResponseDto> receipt
) {
}
