package store.dto.request;

public record OrderRequestDto(
        String name,
        int quantity
) {
    public static OrderRequestDto of(String name, int quantity) {
        return new OrderRequestDto(name, quantity);
    }
}
