package store.constants;

import java.util.Arrays;

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
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요."));
    }

    public boolean isAnswer() {
        return answer;
    }
}
