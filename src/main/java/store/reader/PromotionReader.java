package store.reader;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.domain.Promotion;
import store.util.Reader;

public class PromotionReader {
    private static final String DELIMITER = ",";
    private static final int NAME_INDEX = 0;
    private static final int BUY_COUNT_INDEX = 1;
    private static final int GET_COUNT_INDEX = 2;
    private static final int START_DATE_INDEX = 3;
    private static final int END_DATE_INDEX = 4;

    public List<Promotion> readPromotions(String file) throws IOException {
        List<String> lines = Reader.readFile(file);
        List<Promotion> promotions = new ArrayList<>();
        lines.forEach(line -> {
            promotions.add(parsePromotion(line));
        });
        return promotions;
    }

    private Promotion parsePromotion(String line) {
        String[] parts = line.split(DELIMITER);
        String name = parts[NAME_INDEX];
        int buy = Integer.parseInt(parts[BUY_COUNT_INDEX]);
        int get = Integer.parseInt(parts[GET_COUNT_INDEX]);
        LocalDate startDate = LocalDate.parse(parts[START_DATE_INDEX]);
        LocalDate endDate = LocalDate.parse(parts[END_DATE_INDEX]);
        return new Promotion(name, buy, get, startDate.atStartOfDay(), endDate.atStartOfDay());
    }
}
