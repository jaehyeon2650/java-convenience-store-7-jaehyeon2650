package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.dto.request.OrdersRequestDto;
import store.util.Parser;

public class InputView {
    public OrdersRequestDto getOrders() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String orderList = Console.readLine().trim();
        System.out.println();
        String[] words = orderList.split(",");
        return Parser.parseToOrders(words);
    }
}
