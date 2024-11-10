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
            int itemsPrice = items.calculateItemsPrice(itemName, count);
            int benefit = items.calculateItemsPrice(itemName, paymentRequestDto.get());
            receipts.add(new ReceiptResponseDto(itemName, count, itemsPrice, paymentRequestDto.get(), benefit));
        }
        return makeReceiptsResponseDto(receipts, memberShip);
    }

    private ReceiptsResponseDto makeReceiptsResponseDto(List<ReceiptResponseDto> receipts, boolean memberShip) {
        int totalPrice = 0;
        int totalCount = 0;
        int promotionDiscount = 0;
        int notBenefitPrice = 0;
        for (ReceiptResponseDto receipt : receipts) {
            totalPrice += receipt.itemsPrice();
            totalCount += receipt.totalCount();
            promotionDiscount += receipt.benefit();
            if (receipt.benefit() == 0) {
                notBenefitPrice += receipt.itemsPrice();
            }
        }
        int membershipBenefit = (int) (notBenefitPrice * 0.3);
        if (membershipBenefit > 8000) {
            membershipBenefit = 8000;
        }
        if (memberShip) {
            return new ReceiptsResponseDto(totalPrice, totalCount, promotionDiscount, membershipBenefit,
                    totalPrice - promotionDiscount - membershipBenefit, receipts);
        }
        return new ReceiptsResponseDto(totalPrice, totalCount, promotionDiscount, 0, totalPrice - promotionDiscount,
                receipts);
    }
}
