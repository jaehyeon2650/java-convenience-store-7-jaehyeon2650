package store.domain;

import store.dto.response.PromotionResponseDto;

public enum PromotionResult {
    APPLY_REGULAR_PRICE {
        @Override
        public PromotionResponseDto getResult(int buy, int get, int stockQuantity, int purchase) {
            int set = stockQuantity / (buy + get);
            int buyCount = set * buy;
            int getCount = set * get;
            int extraBuy = (purchase - stockQuantity) + stockQuantity % (buy + get);
            return new PromotionResponseDto(this, buyCount, getCount, extraBuy, 0);
        }

        @Override
        public boolean canApply(int buy, int get, int stockQuantity, int purchase) {
            return stockQuantity < purchase || (stockQuantity == purchase && purchase % (get + buy) == buy);
        }
    },

    REQUIRE_ADDITIONAL_ITEM {
        @Override
        public PromotionResponseDto getResult(int buy, int get, int stockQuantity, int purchase) {
            int set = purchase / (buy + get);
            int buyCount = set * buy + buy;
            int getCount = set * get;
            return new PromotionResponseDto(this, buyCount, getCount, 0, 1);
        }

        @Override
        public boolean canApply(int buy, int get, int stockQuantity, int purchase) {
            return stockQuantity > purchase && purchase % (buy + get) == buy;
        }
    },

    SUCCESS {
        @Override
        public PromotionResponseDto getResult(int buy, int get, int stockQuantity, int purchase) {
            int set = purchase / (buy + get);
            int buyCount = set * buy + purchase % (buy + get);
            int getCount = set * get;
            return new PromotionResponseDto(this, buyCount, getCount, 0, 0);
        }

        @Override
        public boolean canApply(int buy, int get, int stockQuantity, int purchase) {
            return !APPLY_REGULAR_PRICE.canApply(buy, get, stockQuantity, purchase) &&
                    !REQUIRE_ADDITIONAL_ITEM.canApply(buy, get, stockQuantity, purchase);
        }
    },

    NONE {
        @Override
        public PromotionResponseDto getResult(int buy, int get, int stockQuantity, int purchase) {
            return new PromotionResponseDto(this, 0, 0, 0, 0);
        }

        @Override
        public boolean canApply(int buy, int get, int stockQuantity, int purchase) {
            return false;
        }
    };

    public abstract PromotionResponseDto getResult(int buy, int get, int stockQuantity, int purchase);

    public abstract boolean canApply(int buy, int get, int stockQuantity, int purchase);
}
