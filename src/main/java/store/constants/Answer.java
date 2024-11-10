package store.constants;

import java.util.Arrays;
import store.exception.ErrorMessage;
import store.exception.StoreException;

public enum Answer {
    YES("Y", true), NO("N", false);
    private String input;
    private boolean answer;

    Answer(String input, boolean answer) {
        this.input = input;
        this.answer = answer;
    }

    public static Answer findAnswer(String input) {
        return Arrays.stream(Answer.values()).filter(answer -> answer.input.equals(input))
                .findAny()
                .orElseThrow(() -> StoreException.from(ErrorMessage.INVALID_INPUT));
    }

    public boolean isAnswer() {
        return answer;
    }
}
