package store.view;

import java.util.List;
import store.dto.response.ItemResponseDto;
import store.dto.response.ItemsResponseDto;
import store.dto.response.ReceiptResponseDto;
import store.dto.response.ReceiptsResponseDto;

public class OutputView {
    public void printErrorMessage(String errorMessage) {
        System.out.println(errorMessage);
    }

    public void printItemList(ItemsResponseDto itemList) {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.\n");
        List<ItemResponseDto> itemResponseDtos = itemList.itemResponses();
        for (ItemResponseDto itemResponseDto : itemResponseDtos) {
            System.out.printf("- " + itemResponseDto.name() + " %,d원 ", itemResponseDto.price());
            System.out.printf(getItemCount(itemResponseDto), itemResponseDto.quantity());
            printPromotion(itemResponseDto);
        }
        System.out.println();
    }

    public void printReceipts(ReceiptsResponseDto response) {
        printReceiptsHeader();
        printReceiptsInfo(response);
        printBenefitInfo(response);
        printReceiptsSummary(response);
    }

    private String getItemCount(ItemResponseDto itemResponseDto) {
        if (itemResponseDto.quantity() == 0) {
            return "재고 없음";
        }
        return "%,d개";
    }

    private void printPromotion(ItemResponseDto itemResponseDto) {
        if (itemResponseDto.promotion().isPresent()) {
            System.out.print(" " + itemResponseDto.promotion().get());
        }
        System.out.println();
    }

    private void printReceiptsHeader() {
        System.out.println("==============W 편의점================");
        System.out.printf("%-17s%-8s%-8s%n", "상품명", "수량", "금액");
    }

    private static void printReceiptsInfo(ReceiptsResponseDto response) {
        for (ReceiptResponseDto result : response.receipt()) {
            System.out.printf("%-17s%-8d%,-8d%n", result.name(), result.totalCount(), result.itemsPrice());
        }
    }

    private static void printBenefitInfo(ReceiptsResponseDto response) {
        System.out.println("==============증    정===============");
        for (ReceiptResponseDto result : response.receipt()) {
            if (result.benefit() != 0) {
                System.out.printf("%-17s %-8d%n", result.name(), result.getCount());
            }
        }
    }

    private static void printReceiptsSummary(ReceiptsResponseDto response) {
        System.out.println("=====================================");
        System.out.printf("%-17s%-8d%,-10d%n", "총구매액", response.totalCount(), response.totalPrice());
        System.out.printf("%-17s%8s%s%,-11d%n", "행사할인", "", "-", response.promotionDiscount());
        System.out.printf("%-17s%8s%s%,-11d%n", "멤버십할인", "", "-", response.membershipDiscount());
        System.out.printf("%-17s%10s%,-10d%n" + System.lineSeparator(), "내실돈", "", response.payment());
    }
}
