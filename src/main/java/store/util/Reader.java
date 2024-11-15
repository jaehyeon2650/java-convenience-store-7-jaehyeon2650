package store.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Reader {
    private static final int HEADER_LINE = 1;

    public static List<String> readFile(String file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            return reader.lines().skip(HEADER_LINE).toList();
        }
    }
}
