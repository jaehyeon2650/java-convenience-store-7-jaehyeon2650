package store.reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.domain.Promotion;

public class PromotionReader {
    public List<Promotion> readPromotions(String file) throws IOException {
        List<Promotion> promotions = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

        br.readLine();
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
            String[] parts = line.split(",");
            String name = parts[0];
            int buy = Integer.parseInt(parts[1]);
            int get = Integer.parseInt(parts[2]);
            LocalDate startDate = LocalDate.parse(parts[3]);
            LocalDate endDate = LocalDate.parse(parts[4]);
            promotions.add(new Promotion(name, buy, get, startDate, endDate));
        }

        return promotions;
    }
}
