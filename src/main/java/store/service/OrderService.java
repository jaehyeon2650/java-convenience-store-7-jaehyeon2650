package store.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import store.domain.Item;
import store.domain.Items;
import store.dto.response.ItemResponseDto;
import store.dto.response.ItemsResponseDto;
import store.dto.response.PromotionInfoResponseDto;
import store.dto.response.PromotionResponseDto;

public class OrderService {

    private final Items items;

    public OrderService(Items items) {
        this.items = items;
    }

    public ItemsResponseDto getItemList() {
        List<Item> itemList = items.getItems();
        List<ItemResponseDto> list = itemList.stream().map(ItemResponseDto::of).toList();
        return ItemsResponseDto.from(list);
    }

    public PromotionInfoResponseDto getOrderInfo(String name, int count, LocalDateTime orderDate) {
        items.validatePurchase(name, count);
        PromotionResponseDto promotionResult = items.getPromotionResult(name, count, orderDate);
        return PromotionInfoResponseDto.of(name, promotionResult);
    }

}
