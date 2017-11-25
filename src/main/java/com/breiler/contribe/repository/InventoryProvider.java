package com.breiler.contribe.repository;

import com.breiler.contribe.model.Book;
import com.breiler.contribe.model.Inventory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InventoryProvider {

    private final DecimalFormat decimalFormat;

    InventoryProvider() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        String pattern = "#,##0.0#";
        decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);
    }

    public List<Inventory> fetchInventory() {
        try {
            List<String> list = IOUtils.readLines(InventoryProvider.class.getResourceAsStream("/bookstoredata.txt"));
            return list.stream()
                    .map(this::convertLineToInventory)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Couldn't read test data", e);
        }
    }

    private Inventory convertLineToInventory(String line) {
        String[] splittedLine = StringUtils.split(line, ";");
        if (splittedLine.length < 4) {
            throw new RuntimeException("Couldn't parse line '" + line + "', expected 3 columns but found " + splittedLine.length);
        }

        try {
            BigDecimal price = (BigDecimal) decimalFormat.parse(splittedLine[2]);

            Book book = Book.builder()
                    .title(splittedLine[0])
                    .author(splittedLine[1])
                    .price(price)
                    .build();

            return Inventory.builder()
                    .book(book)
                    .quantity(Long.valueOf(splittedLine[3]))
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException("Couldn't parse the price as big decimal", e);
        }
    }
}
