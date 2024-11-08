package store.reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.domain.Item;
import store.domain.Promotion;

public class ItemReader {

    public List<Item> readItems(String file, List<Promotion> promotions) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        List<Item> items = new ArrayList<>();
        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            String name = parts[0];
            int price = Integer.parseInt(parts[1]);
            int quantity = Integer.parseInt(parts[2]);
            String promotionsName = parts[3];

            Optional<Promotion> promotionOptional = promotions.stream()
                    .filter(promotion -> promotion.getName().equals(promotionsName)).findAny();
            if (promotionOptional.isPresent()) {
                items.add(new Item(name, price, quantity, promotionOptional.get()));
            }
            items.add(new Item(name, price, quantity, null));
        }
        return items;
    }
}
