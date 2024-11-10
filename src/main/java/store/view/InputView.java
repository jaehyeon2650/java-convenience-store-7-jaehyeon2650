package store.view;

import static store.exception.ErrorMessage.INVALID_INPUT_FORMAT;

import camp.nextstep.edu.missionutils.Console;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.constants.Answer;
import store.dto.request.OrdersRequestDto;
import store.exception.StoreException;
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

    public boolean chooseAddFreeItem(String name, int count) {
        System.out.println("현재 " + name + "은(는) " + count + "개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
        return getAnswer();
    }

    public boolean chooseExtraPayment(String name, int count) {
        System.out.println("현재 " + name + " " + count + "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
        return getAnswer();
    }

    public boolean chooseMemberShip() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        return getAnswer();
    }

    public boolean chooseRotate() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        return getAnswer();
    }

    private boolean getAnswer() {
        String answer = Console.readLine().trim();
        System.out.println();
        return Answer.findAnswer(answer).isAnswer();
    }

    private static class Validator {
        private static final Pattern ITEM_PATTERN = Pattern.compile("^\\[\\s*[^\\[]+\\s*-\\s*\\d+\\s*\\]$");

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
