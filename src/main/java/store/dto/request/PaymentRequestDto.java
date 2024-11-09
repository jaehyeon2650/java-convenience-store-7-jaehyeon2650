package store.dto.request;

public record PaymentRequestDto(
        String name,
        int buy,
        int get
) {
    public static PaymentRequestDto of(String name, int buy, int get) {
        return new PaymentRequestDto(name, buy, get);
    }
}
