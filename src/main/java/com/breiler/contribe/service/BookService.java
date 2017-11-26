package com.breiler.contribe.service;


import com.breiler.contribe.model.Book;
import com.breiler.contribe.model.Stock;
import com.breiler.contribe.repository.BookRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findByQuery(final String query) {
        String q = StringUtils.defaultString(query);
        return bookRepository.findByTitleContainingOrAuthorContaining(q, q);
    }

    public Book create(Book book) {
        book.setStock(new Stock());
        return bookRepository.save(book);
    }

    public Book findById(Long bookId) {
        return bookRepository.findOne(bookId);
    }
}
