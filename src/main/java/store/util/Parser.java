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
    private static final String DELIMITER = ",";
    private static final int ITEM_NAME_INDEX = 0;
    private static final int ITEM_COUNT_INDEX = 1;

    public static OrdersRequestDto makeOrdersRequest(String input) {
        String[] words = input.split(DELIMITER);
        Validator.validateInputForm(words);
        return parseToOrders(words);
    }

    private static OrdersRequestDto parseToOrders(String[] words) {
        List<OrderRequestDto> orderList = Arrays.stream(words)
                .map(word -> {
                    String[] order = word.substring(1, word.length() - 1).trim().split("-");
                    Validator.validateSplitResultSize(order);
                    return new OrderRequestDto(order[ITEM_NAME_INDEX].trim(),
                            Validator.validateNumber(order[ITEM_COUNT_INDEX].trim()));
                })
                .toList();
        return new OrdersRequestDto(orderList);
    }

    private static class Validator {
        private static final Pattern ITEM_PATTERN = Pattern.compile("^\\[\\s*[^\\[]+\\s*-\\s*\\d+\\s*\\]$");
        private static final int NUMBER_MIN = 0;
        private static final int RESULT_SIZE = 2;

        public static int validateNumber(String number) {
            int result = Integer.parseInt(number);
            if (result <= NUMBER_MIN) {
                throw StoreException.from(INVALID_INPUT);
            }
            return result;
        }

        public static void validateSplitResultSize(String[] order) {
            if (order.length != RESULT_SIZE) {
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
