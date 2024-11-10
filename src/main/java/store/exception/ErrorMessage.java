package store.exception;

public enum ErrorMessage {
    INVALID_INPUT_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    INVALID_ITEM("존재하지 않는 상품입니다. 다시 입력해 주세요."),
    INVALID_QUANTITY("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    INVALID_INPUT("잘못된 입력입니다. 다시 입력해 주세요."),
    FILE_PROBLEM("파일을 읽는 중에 실패하였습니다. 파일명을 확인해주세요.");
    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
