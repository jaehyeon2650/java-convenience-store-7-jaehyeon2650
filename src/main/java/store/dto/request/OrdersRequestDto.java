package store.dto.request;

import java.util.List;

public record OrdersRequestDto(
        List<OrderRequestDto> orderList
) {
    public static OrdersRequestDto from(List<OrderRequestDto> orderList) {
        return new OrdersRequestDto(orderList);
    }
}
