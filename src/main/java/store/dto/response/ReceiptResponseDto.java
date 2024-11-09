package store.dto.response;

public record ReceiptResponseDto(
        String name,
        int totalCount,
        int itemsPrice,
        int getCount,
        int benefit
) {
}
