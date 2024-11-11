package store.util;

import static store.exception.ErrorMessage.INVALID_INPUT;
import static store.exception.ErrorMessage.INVALID_INPUT_FORMAT;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.dto.request.OrderRequestDto;
import store.dto.request.OrdersRequestDto;
import store.exception.StoreException;

public class Parser {
    public static OrdersRequestDto makeOrdersRequest(String input) {
        String[] words = input.split(",");
        Validator.validateInputForm(words);
        return parseToOrders(words);
    }

    private static OrdersRequestDto parseToOrders(String[] words) {
        List<OrderRequestDto> orderList = Arrays.stream(words)
                .map(word -> {
                    String[] order = word.substring(1, word.length() - 1).trim().split("-");
                    Validator.validateSplitResultSize(order);
                    return new OrderRequestDto(order[0].trim(), Validator.validateNumber(order[1].trim()));
                })
                .toList();
        return new OrdersRequestDto(orderList);
    }

    private static class Validator {
        private static final Pattern ITEM_PATTERN = Pattern.compile("^\\[\\s*[^\\[]+\\s*-\\s*\\d+\\s*\\]$");

        public static int validateNumber(String number) {
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

        public static void validateInputForm(String[] words) {
            for (String word : words) {
                Matcher matcher = ITEM_PATTERN.matcher(word.trim());
                if (!matcher.matches()) {
                    throw StoreException.from(INVALID_INPUT_FORMAT);
                }
            }
        }
    }
}
