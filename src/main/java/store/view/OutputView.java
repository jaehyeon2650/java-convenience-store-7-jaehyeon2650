package store.view;

import java.util.List;
import store.dto.response.ItemResponseDto;
import store.dto.response.ItemsResponseDto;

public class OutputView {

    public void printHelloMessage() {
        System.out.println("안녕하세요. W편의점입니다.");
    }

    public void printItemList(ItemsResponseDto itemList) {
        System.out.println("현재 보유하고 있는 상품입니다.");
        System.out.println();
        List<ItemResponseDto> itemResponseDtos = itemList.itemResponses();
        for (ItemResponseDto itemResponseDto : itemResponseDtos) {
            System.out.printf("- " + itemResponseDto.name() + " %,d원 ", itemResponseDto.price());
            System.out.printf(getItemCount(itemResponseDto), itemResponseDto.quantity());
            printPromotion(itemResponseDto);
        }
        System.out.println();
    }

    private String getItemCount(ItemResponseDto itemResponseDto) {
        if (itemResponseDto.quantity() == 0) {
            return "재고 없음";
        }
        return "%,d개";
    }

    private void printPromotion(ItemResponseDto itemResponseDto) {
        if (itemResponseDto.promotion() != null) {
            System.out.print(" " + itemResponseDto.promotion());
        }
        System.out.println();
    }
}
