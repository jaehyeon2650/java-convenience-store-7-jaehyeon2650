package store.dto.request;

import java.util.List;

public record OrdersRequestDto(
        List<OrderRequestDto> orderList
) {
}
