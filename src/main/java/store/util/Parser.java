package store.util;

import static store.exception.ErrorMessage.INVALID_INPUT;
import static store.exception.ErrorMessage.INVALID_INPUT_FORMAT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import store.dto.request.OrderRequestDto;
import store.dto.request.OrdersRequestDto;
import store.exception.StoreException;

public class Parser {
    public static OrdersRequestDto parseToOrders(String[] words) {
        List<OrderRequestDto> orderList = new ArrayList<>();
        Arrays.stream(words).forEach(
                word -> {
                    word = word.substring(1, word.length() - 1);
                    String[] order = word.trim().split("-");
                    Validator.validateSplitResultSize(order);
                    orderList.add(new OrderRequestDto(order[0].trim(), Validator.validateNumber(order[1].trim())));
                }
        );
        return new OrdersRequestDto(orderList);
    }

    private static class Validator {
        public static int validateNumber(String number) {
            if (!number.matches("\\d+")) {
                throw StoreException.from(INVALID_INPUT);
            }
            return changeTONumber(number);
        }

        private static int changeTONumber(String number) {
            int result = Integer.parseInt(number);
            if (result <= 0) {
                throw StoreException.from(INVALID_INPUT);
            }
            return result;
        }

        public static void validateSplitResultSize(String[] order) {
            if (order.length != 2) {
                throw StoreException.from(INVALID_INPUT_FORMAT);
            }
        }
    }
}
