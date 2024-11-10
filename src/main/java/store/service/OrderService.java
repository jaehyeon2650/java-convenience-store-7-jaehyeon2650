package store.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import store.domain.Item;
import store.domain.Items;
import store.dto.request.PaymentRequestDto;
import store.dto.response.ItemResponseDto;
import store.dto.response.ItemsResponseDto;
import store.dto.response.PromotionInfoResponseDto;
import store.dto.response.PromotionResponseDto;
import store.dto.response.ReceiptResponseDto;
import store.dto.response.ReceiptsResponseDto;

public class OrderService {

    private final Items items;

    public OrderService(Items items) {
        this.items = items;
    }

    public ItemsResponseDto getItemList() {
        List<Item> itemList = items.getItems();
        List<ItemResponseDto> list = itemList.stream().map(ItemResponseDto::new).toList();
        return new ItemsResponseDto(list);
    }

    public PromotionInfoResponseDto getOrderInfo(String name, int count, LocalDateTime orderDate) {
        items.validatePurchase(name, count);
        PromotionResponseDto promotionResult = items.getPromotionResult(name, count, orderDate);
        return new PromotionInfoResponseDto(name, promotionResult);
    }

    public ReceiptsResponseDto payment(List<PaymentRequestDto> paymentRequestDtos, boolean memberShip) {
        List<ReceiptResponseDto> receipts = new ArrayList<>();
        for (PaymentRequestDto paymentRequestDto : paymentRequestDtos) {
            String itemName = paymentRequestDto.name();
            int count = paymentRequestDto.buy() + paymentRequestDto.get();
            items.purchaseItem(itemName, count);
            int itemsPrice = items.calculateItemsPrice(itemName, count);
            int benefit = items.calculateItemsPrice(itemName, paymentRequestDto.get());
            receipts.add(new ReceiptResponseDto(itemName, count, itemsPrice, paymentRequestDto.get(), benefit));
        }
        return makeReceiptsResponseDto(receipts, memberShip);
    }

    private ReceiptsResponseDto makeReceiptsResponseDto(List<ReceiptResponseDto> receipts, boolean memberShip) {
        int totalPrice = calculateTotalPrice(receipts);
        int totalCount = calculateTotalCount(receipts);
        int promotionDiscount = calculatePromotionDiscount(receipts);
        int notBenefitPrice = calculateNotBenefitPrice(receipts);
        int membershipBenefit = calculateMembershipBenefit(notBenefitPrice);
        if (memberShip) {
            return createResponseDto(totalPrice, totalCount, promotionDiscount, membershipBenefit, receipts);
        }
        return createResponseDto(totalPrice, totalCount, promotionDiscount, 0, receipts);
    }

    private int calculateTotalPrice(List<ReceiptResponseDto> receipts) {
        return receipts.stream().mapToInt(ReceiptResponseDto::itemsPrice).sum();
    }

    private int calculateTotalCount(List<ReceiptResponseDto> receipts) {
        return receipts.stream().mapToInt(ReceiptResponseDto::totalCount).sum();
    }

    private int calculatePromotionDiscount(List<ReceiptResponseDto> receipts) {
        return receipts.stream().mapToInt(ReceiptResponseDto::benefit).sum();
    }

    private int calculateNotBenefitPrice(List<ReceiptResponseDto> receipts) {
        return receipts.stream()
                .filter(receipt -> receipt.benefit() == 0)
                .mapToInt(ReceiptResponseDto::itemsPrice)
                .sum();
    }

    private int calculateMembershipBenefit(int notBenefitPrice) {
        int membershipBenefit = (int) (notBenefitPrice * 0.3);
        return Math.min(membershipBenefit, 8000);
    }

    private ReceiptsResponseDto createResponseDto(int totalPrice, int totalCount, int promotionDiscount,
                                                  int membershipBenefit, List<ReceiptResponseDto> receipts) {
        return new ReceiptsResponseDto(totalPrice, totalCount, promotionDiscount, membershipBenefit,
                totalPrice - promotionDiscount - membershipBenefit, receipts);
    }
}
