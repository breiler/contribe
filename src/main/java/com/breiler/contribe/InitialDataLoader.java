package com.breiler.contribe;

import com.breiler.contribe.model.Book;
import com.breiler.contribe.model.Stock;
import com.breiler.contribe.service.BookService;
import com.breiler.contribe.service.StockService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InitialDataLoader {

    private DecimalFormat decimalFormat;
    private BookService bookService;
    private StockService stockService;

    @Autowired
    InitialDataLoader(BookService bookService, StockService stockService) {
        this.bookService = bookService;
        this.stockService = stockService;
        createDecimalFormat();
    }

    private void createDecimalFormat() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        String pattern = "#,##0.0#";
        decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);
    }

    public void loadInitialDataIfEmpty() {
        List<Book> books = bookService.findByQuery(null);
        if (books.isEmpty()) {
            List<Stock> stockList = getStockFromFile();
            stockList.forEach(stock -> {
                Book book = bookService.create(stock.getBook());
                stockService.setStock(book.getId(), stock.getQuantity());
            });
        }
    }

    private List<Stock> getStockFromFile() {
        try {
            List<String> list = IOUtils.readLines(new URL("https://raw.githubusercontent.com/contribe/contribe/dev/bookstoredata/bookstoredata.txt").openStream());
            return list.stream()
                    .map(this::convertLineToInventory)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Couldn't read test data", e);
        }
    }

    private Stock convertLineToInventory(String line) {
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

            return Stock.builder()
                    .book(book)
                    .quantity(Long.valueOf(splittedLine[3]))
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException("Couldn't parse the price as big decimal", e);
        }
    }
}
