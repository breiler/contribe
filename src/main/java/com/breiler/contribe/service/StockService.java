package com.breiler.contribe.service;


import com.breiler.contribe.model.Book;
import com.breiler.contribe.model.Stock;
import com.breiler.contribe.repository.BookRepository;
import com.breiler.contribe.repository.StockRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final BookRepository bookRepository;


    @Autowired
    public StockService(StockRepository stockRepository, BookRepository bookRepository) {
        this.stockRepository = stockRepository;
        this.bookRepository = bookRepository;
    }

    public List<Stock> findAll() {
        return Lists.newArrayList(stockRepository.findAll());
    }

    public Stock setStock(Long bookId, Long quantity) {
        Stock stock = this.stockRepository.findByBookId(bookId);

        if (stock == null) {
            Book book = bookRepository.findOne(bookId);
            if (book == null) {
                throw new IllegalArgumentException("Couldn't find book with bookId " + bookId);
            }
            stock = new Stock();
            stock.setBook(book);
            stock.setQuantity(quantity);
        } else {
            stock.setQuantity(quantity);
        }

        return this.stockRepository.save(stock);
    }
}
