package store.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import store.dto.request.OrderRequestDto;
import store.dto.request.OrdersRequestDto;

public class Parser {
    public static OrdersRequestDto parseToOrders(String[] words) {
        List<OrderRequestDto> orderList = new ArrayList<>();
        Arrays.stream(words).forEach(
                word -> {
                    word = word.substring(1, word.length() - 1);
                    String[] order = word.trim().split("-");
                    orderList.add(OrderRequestDto.of(order[0], Integer.parseInt(order[1])));
                }
        );
        return OrdersRequestDto.from(orderList);
    }
}
