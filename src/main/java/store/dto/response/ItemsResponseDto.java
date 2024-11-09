package store.dto.response;

import java.util.List;

public record ItemsResponseDto(
        List<ItemResponseDto> itemResponses
) {
    public static ItemsResponseDto from(List<ItemResponseDto> itemResponses) {
        return new ItemsResponseDto(itemResponses);
    }
}
