package store.util;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static store.exception.ErrorMessage.INVALID_INPUT_FORMAT;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.dto.request.OrderRequestDto;
import store.dto.request.OrdersRequestDto;
import store.exception.StoreException;

class ParserTest {

    @ParameterizedTest
    @ValueSource(strings = {"[a-1},[b-2],[c-2]", "{a-1}", "a-1"})
    @DisplayName("makeOrdersRequest - [ - ]의 형식으로 주문을 받지 않으면 예외가 발생한다.")
    void invalidInputFormat(String input) {
        // when & then
        assertThatThrownBy(() -> Parser.makeOrdersRequest(input))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(INVALID_INPUT_FORMAT.getMessage());

    }

    @Test
    @DisplayName("makeOrdersRequest - 각 대괄호 안에 -를 두번쓰면 예외가 발생한다.")
    void invalidInputFormatWithUseDelimiterTwice() {
        // given
        String input = "[a-1-1]";
        // when & then
        assertThatThrownBy(() -> Parser.makeOrdersRequest(input))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(INVALID_INPUT_FORMAT.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"[a-a]", "[a-a2]", "[a-a2a]"})
    @DisplayName("makeOrdersRequest - 주문 개수에 관한 입력을 숫자가 아닌 문자가 입력이 되면 예외가 발생한다.")
    void notNumber(String input) {
        // when & then
        assertThatThrownBy(() -> Parser.makeOrdersRequest(input))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(INVALID_INPUT_FORMAT.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"[a-0]", "[a--1]"})
    @DisplayName("makeOrdersRequest - 숫자는 항상 1보다 크거나 같아야한다. 아닐 경우 예외가 발생한다.")
    void numberShouldGreaterThanZero(String input) {
        // when & then
        assertThatThrownBy(() -> Parser.makeOrdersRequest(input))
                .isInstanceOf(StoreException.class);
    }

    @Test
    @DisplayName("makeOrdersRequest - 정상적인 입력에선 OrdersRequestDto에 정보를 담아 반환한다.")
    void successParse() {
        // given
        String input = "[a-1],[b-2],[c-3]";
        // when
        OrdersRequestDto result = Parser.makeOrdersRequest(input);
        // then
        List<OrderRequestDto> list = result.orderList();
        boolean hasItemA = list.stream()
                .anyMatch(orderRequestDto -> orderRequestDto.name().equals("a") && orderRequestDto.quantity() == 1);
        boolean hasItemB = list.stream()
                .anyMatch(orderRequestDto -> orderRequestDto.name().equals("b") && orderRequestDto.quantity() == 2);
        boolean hasItemC = list.stream()
                .anyMatch(orderRequestDto -> orderRequestDto.name().equals("c") && orderRequestDto.quantity() == 3);
        assertTrue(hasItemA);
        assertTrue(hasItemB);
        assertTrue(hasItemC);
    }
}