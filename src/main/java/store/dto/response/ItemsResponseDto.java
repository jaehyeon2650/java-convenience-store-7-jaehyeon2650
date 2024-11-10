package store.dto.response;

import java.util.List;

public record ItemsResponseDto(
        List<ItemResponseDto> itemResponses
) {
}
