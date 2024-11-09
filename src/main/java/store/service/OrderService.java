package store.service;

import store.domain.Items;
import store.dto.response.ItemResponseDto;
import store.dto.response.ItemsResponseDto;

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

}
