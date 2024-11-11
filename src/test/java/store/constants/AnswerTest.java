package store.constants;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.exception.ErrorMessage.INVALID_INPUT;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import store.exception.StoreException;

class AnswerTest {

    @ParameterizedTest
    @ValueSource(strings = {"a", "y", "n", "1"})
    @DisplayName("findAnswer - Y/N이 아닌 다른 입력이 들어왔을 경우 예외가 발생한다.")
    void invalidInput(String input) {
        // when & then
        assertThatThrownBy(() -> Answer.findAnswer(input))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(INVALID_INPUT.getMessage());
    }

    @ParameterizedTest
    @CsvSource({"Y,YES", "N,NO"})
    @DisplayName("findAnswer - 정상적인 값에 대해서는 정확한 Enum을 반환한다.")
    void successFindAnswer(String input, Answer expectedAnswer) {
        // when
        Answer answer = Answer.findAnswer(input);
        // then
        assertThat(answer).isEqualTo(expectedAnswer);
    }
}