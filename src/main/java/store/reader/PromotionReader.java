package store.reader;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.domain.Promotion;
import store.util.Reader;

public class PromotionReader {

    public List<Promotion> readPromotions(String file) throws IOException {
        List<String> lines = Reader.readFile(file);
        List<Promotion> promotions = new ArrayList<>();
        lines.forEach(line -> {
            promotions.add(parsePromotion(line));
        });
        return promotions;
    }

    private Promotion parsePromotion(String line) {
        String[] parts = line.split(",");
        String name = parts[0];
        int buy = Integer.parseInt(parts[1]);
        int get = Integer.parseInt(parts[2]);
        LocalDate startDate = LocalDate.parse(parts[3]);
        LocalDate endDate = LocalDate.parse(parts[4]);
        return new Promotion(name, buy, get, startDate.atStartOfDay(), endDate.atStartOfDay());
    }
}
