package store.exception;

public class StoreException extends IllegalArgumentException {
    public static final String PREFIX = "[ERROR] ";

    private StoreException(ErrorMessage errorMessage) {
        super(PREFIX + errorMessage.getMessage());
    }

    public static StoreException from(ErrorMessage errorMessage) {
        return new StoreException(errorMessage);
    }
}
