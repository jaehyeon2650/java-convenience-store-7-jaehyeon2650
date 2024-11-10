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
                    Validator.validateSplitResultSize(order);
                    Validator.validateNumber(order[1]);
                    orderList.add(new OrderRequestDto(order[0], Integer.parseInt(order[1])));
                }
        );
        return new OrdersRequestDto(orderList);
    }

    private static class Validator {
        public static void validateNumber(String number) {
            if (!number.matches("\\d+")) {
                throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
            }
        }

        public static void validateSplitResultSize(String[] order) {
            if (order.length != 2) {
                throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
            }
        }
    }
}
