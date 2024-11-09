package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.dto.request.OrdersRequestDto;
import store.util.Parser;

public class InputView {
    public OrdersRequestDto getOrders() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String orderList = Console.readLine().trim();
        System.out.println();
        String[] words = orderList.split(",");
        Validator.validateInputForm(words);
        return Parser.parseToOrders(words);
    }
    private static class Validator {
        private static final Pattern ITEM_PATTERN = Pattern.compile("^\\[[^\\[]+-\\d+\\]$");

        public static void validateInputForm(String[] words) {
            for (String word : words) {
                Matcher matcher = ITEM_PATTERN.matcher(word.trim());
                if (!matcher.matches()) {
                    throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
                }
            }
        }
    }
}
